package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.ebi.biosamples.cohortatlas.KeycloakTestContainers;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortSearchRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CohortServiceIntegrationTest extends KeycloakTestContainers {

  @Autowired
  private CohortService cohortService;
  @Autowired
  private CohortSearchRepository cohortSearchRepository;

  @Test
  public void createCohortWithAccession() {
    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(
        PageRequest.of(0, Integer.MAX_VALUE), null, "cohortName", Collections.emptyList());
    long countOfCohortAlreadyInDB = page.get().count();

    Cohort cohort1 = new Cohort();
    String cohortAccession = "BSC" + String.format("%06d", ++countOfCohortAlreadyInDB);
    cohort1.setCohortName("Creating cohort-1 with accession: " + cohortAccession);
    cohort1.setAccession(cohortAccession);
    cohort1.setDescription("Test cohort for UKB containing text Cohort creation IntegrationTest");
    cohort1.setAcronym("UKB");
    cohortService.saveCohort(cohort1);

    Cohort cohort2 = new Cohort();
    cohortAccession = "BSC" + String.format("%06d", ++countOfCohortAlreadyInDB);
    cohort2.setCohortName("Creating cohort-2 with accession: " + cohortAccession);
    cohort2.setAccession(cohortAccession);
    cohort2.setDescription(" Cohort creation IntegrationTest should work with free text");
    cohort2.setAcronym("CHILD");
    cohortService.saveCohort(cohort2);


    Cohort cohortFetchedFromDB = cohortService.getCohortById(cohort1.getAccession());
    assertThat(cohortFetchedFromDB.getAccession()).isEqualTo(cohort1.getAccession());
    assertThat(cohortFetchedFromDB.getCohortName()).isEqualTo("Creating cohort-1 with accession: " + cohort1.getAccession());

    cohortFetchedFromDB = cohortService.getCohortById(cohort2.getAccession());
    assertThat(cohortFetchedFromDB.getAccession()).isEqualTo(cohort2.getAccession());
    assertThat(cohortFetchedFromDB.getCohortName()).isEqualTo("Creating cohort-2 with accession: " + cohort2.getAccession());
  }

  @Test
  public void facetSearchCohort() {
    List<Facet> facets = cohortSearchRepository.getFacets("", new ArrayList<>());
    assertThat(facets != null);
  }
}
