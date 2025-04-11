package uk.ac.ebi.biosamples.cohortatlas.harmonisation.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.config.CohortAtlasProperties;
import uk.ac.ebi.biosamples.cohortatlas.harmonisation.HarmonisationService;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;
import uk.ac.ebi.biosamples.cohortatlas.model.Suggestion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeminiHarmonisationService implements HarmonisationService {
  private final ObjectMapper objectMapper;
  private final ChatLanguageModel chatLanguageModel;
  private final CohortAtlasProperties cohortAtlasProperties;
  private static final String PROMPT = """
        You are a data harmonization AI. You have been given a standard dictionary with columns "TERM" and "DESCRIPTION" \\
        and an input data with columns "TERM" and "DESCRIPTION". Map each of the "TERM" in input data to a "TERM" in \\
        standard dictionary. Return mapping as a new key "MAPPING".
      
        Dictionary:
        %s                                                                                                                                                                                                                                                                                              Information about an individual's association with a religion, denomiation, or sub- or non-denominational religious group.
      
        Input Data:
        %s
      
        Provide the harmonized output as a JSON list.
      """;

  public GeminiHarmonisationService(ObjectMapper objectMapper, ChatLanguageModel chatLanguageModel, CohortAtlasProperties cohortAtlasProperties) {
    this.objectMapper = objectMapper;
    this.chatLanguageModel = chatLanguageModel;
    this.cohortAtlasProperties = cohortAtlasProperties;
  }

  @Override
  public List<Field> harmoniseDictionary(String accession, List<Field> dictionary) {
    List<HarmonisingField> standardDictionary = readDictionaryAndReturnAsJsonString(cohortAtlasProperties.getStandardDictionaryPath());
    List<HarmonisingField> suggestions;
    try {
      String standardDictionaryString = objectMapper.writeValueAsString(standardDictionary);
      String dictionaryString = objectMapper.writeValueAsString(fieldsToHarmonisingFields(dictionary));
      String prompt = PROMPT.formatted(standardDictionaryString, dictionaryString);
      String out = chatLanguageModel.chat(prompt);
      suggestions = new ObjectMapper().readValue(out.substring(8, out.length() - 3), new TypeReference<>() {});
    } catch (IOException e) {
      log.error("Failed to process dictionaries", e);
      throw new RuntimeException(e);
    }

    Map<String, String> map = suggestions.stream()
        .collect(Collectors.toUnmodifiableMap(HarmonisingField::getTerm, HarmonisingField::getMapping));

    //todo create new dictionary instead of mutating the input
    for (Field f : dictionary) {
      String suggestion = map.get(f.getName());
      f.setAnnotation(suggestion);
      f.setSuggestions(Collections.singletonList(new Suggestion(suggestion, 90.0f)));
    }

    return dictionary;
  }

  private List<HarmonisingField> readDictionaryAndReturnAsJsonString(String fileName) {
    CsvMapper mapper = new CsvMapper();
    CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',').withComments();

    try {
      InputStream in = new ClassPathResource(fileName).getInputStream();
      MappingIterator<HarmonisingField> csvIterator = mapper.readerWithTypedSchemaFor(HarmonisingField.class).with(csvSchema)
          .readValues(in);
      return csvIterator.readAll();
    } catch (IOException e) {
      log.error("Failed to read/parse standard dictionary from file: {}", fileName, e);
      throw new RuntimeException(e);
    }
  }

  private List<HarmonisingField> fieldsToHarmonisingFields(List<Field> fields) {
    return fields.stream().map(f -> new HarmonisingField(f.getName(), f.getDescription(), null)).toList();
  }
}
