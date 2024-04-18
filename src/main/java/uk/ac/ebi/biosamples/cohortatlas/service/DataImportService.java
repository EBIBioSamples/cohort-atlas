package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;

import java.util.List;

@Service
public class DataImportService {
  private static final String IMPORT_URL = "https://raw.githubusercontent.com/EBIBioSamples/cohort-atlas/main/src/main/resources/env/init_db_schema.json";
  private final CohortRepository cohortRepository;
  private final WebClient webClient;

  public DataImportService(CohortRepository cohortRepository) {
    this.cohortRepository = cohortRepository;
    this.webClient = WebClient.create();
  }

  public void importCohorts() {
    String cohorts = webClient.get()
        .uri(IMPORT_URL)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class)
        .block();
    System.out.println(cohorts);

    List<Cohort> cohortList = null;
    cohortRepository.saveAll(cohortList);
  }
}
