package uk.ac.ebi.biosamples.cohortatlas.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortSearchRepository;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
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
    Reader reader = new InputStreamReader(file.getInputStream());
    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();


    CsvMapper mapper = new CsvMapper();
    List<Field> dictionary = mapper.readValue(file.getInputStream(), new TypeReference<List<Field>>(){});


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
