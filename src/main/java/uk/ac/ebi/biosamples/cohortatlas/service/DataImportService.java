package uk.ac.ebi.biosamples.cohortatlas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ebi.biosamples.cohortatlas.cohort.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.cohort.CohortRepository;

import java.util.List;

@Service
@Slf4j
public class DataImportService {
  private static final String IMPORT_URL = "https://raw.githubusercontent.com/EBIBioSamples/cohort-atlas/main/src/main/resources/env/cohorts.json";
  private final CohortRepository cohortRepository;
  private final ObjectMapper objectMapper;
  private final WebClient webClient;


  public DataImportService(CohortRepository cohortRepository, ObjectMapper objectMapper) {
    this.cohortRepository = cohortRepository;
    this.objectMapper = objectMapper;
    this.webClient = WebClient.create();
  }

  public void importCohorts() {
    String cohorts = webClient.get()
        .uri(IMPORT_URL)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class)
        .block();
    try {
      List<Cohort> cohortList = objectMapper.readValue(cohorts, new TypeReference<List<Cohort>>() {
      });
      cohortRepository.deleteAll();
      cohortRepository.saveAll(cohortList);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert imported cohorts to entity: " + cohorts, e);
    }
  }
}
