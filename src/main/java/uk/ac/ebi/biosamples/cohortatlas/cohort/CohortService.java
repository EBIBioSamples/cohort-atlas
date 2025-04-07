package uk.ac.ebi.biosamples.cohortatlas.cohort;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.io.IOException;
import java.util.List;

@Service
public class CohortService {
  private final CohortRepository cohortRepository;
  private final CohortSearchRepository cohortSearchRepository;

  public CohortService(CohortRepository cohortRepository, CohortSearchRepository cohortSearchRepository) {
    this.cohortRepository = cohortRepository;
    this.cohortSearchRepository = cohortSearchRepository;
  }

  public Cohort saveCohort(Cohort cohort) {
    return cohortRepository.save(cohort);
  }

  public Page<Cohort> searchCohorts(Pageable pageRequest, String text, List<String> filters, String sort) {
    return cohortSearchRepository.findPageWithFilters(pageRequest, text, sort, filters);
  }

  @NonNull
  public Cohort getCohortById(String id) {
    return cohortRepository.findById(id).orElseThrow(() -> new RuntimeException("Cohort does not exist: " + id));
  }

  public void deleteCohort(String id) {
    cohortRepository.deleteById(id);
  }

  public void saveDictionaryFields(String accession, List<Field> fields) {
    Cohort cohort = cohortRepository.findById(accession).orElseThrow(() -> new RuntimeException("Cohort does not exist: " + accession));
    cohort.setDictionary(fields);
    cohortRepository.save(cohort);
  }

  public void saveDictionaryFields(String accession, MultipartFile file) throws IOException {
//    Reader reader = new InputStreamReader(file.getInputStream());
//    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

//    CsvSchema schema = CsvSchema.builder().addColumn("parentCategoryCode")
//        .addColumn("code").addColumn("name").addColumn("description").build();
//    ObjectReader oReader = mapper.readerFor(OfferTemplateCategory.class).with(schema);
//    try (Reader reader = new FileReader(file)) {
//      MappingIterator mi = oReader.readValues(reader);
//
//      while (mi.hasNext())
//      {
//        System.out.println(mi.next());
//      }
//
//    }

    CsvMapper mapper = new CsvMapper();

    CsvSchema csvSchema = mapper
        .typedSchemaFor(Field.class)
        .withHeader()
        .withColumnSeparator(',')
        .withComments();

    MappingIterator<Field> csvIterator = mapper.readerWithTypedSchemaFor(Field.class).with(csvSchema)
        .readValues(file.getInputStream());

    List<Field> dictionary = csvIterator.readAll();

//    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//    List<Field> dictionary = mapper.readValue(file.getInputStream(), new TypeReference<List<Field>>(){});
    saveDictionaryFields(accession, dictionary);
  }

  public Pageable getPageRequest(Integer page, Integer size) {
    if (page == null || page < 0) {
      page = 0;
    }
    if (size == null || size < 1) {
      size = 10;
    }
    return PageRequest.of(page, size);
  }
}
