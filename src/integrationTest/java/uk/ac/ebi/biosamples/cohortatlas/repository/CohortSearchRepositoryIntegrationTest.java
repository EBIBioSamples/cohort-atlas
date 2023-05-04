package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.Collections;


@SpringBootTest
@ActiveProfiles("test")
class CohortSearchRepositoryIntegrationTest {
  @Autowired
  private CohortService cohortService;
  @Autowired
  private CohortRepository cohortSearchRepository;


  @Test
  void findPage() {
    Cohort cohort = new Cohort();
    cohort.setCohortName("SearchRepositoryIntegrationTest_freeTextSearch_1");
    cohort.setDescription("Test cohort for UKB containing text CohortSearchRepositoryIntegrationTest");
    cohort.setAcronym("UKB");
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("SearchRepositoryIntegrationTest_freeTextSearch_2");
    cohort.setDescription("CohortSearchRepositoryIntegrationTest should work with free text");
    cohort.setAcronym("CHILD");
    cohortService.saveCohort(cohort);

    Page<Cohort> page = cohortSearchRepository.findByFilters(PageRequest.of(0, 5), "CohortSearchRepositoryIntegrationTest", "cohortName", Collections.emptyList());
    Assertions.assertTrue(page.getSize() > 1);
    Assertions.assertEquals("SearchRepositoryIntegrationTest_freeTextSearch_1", page.getContent().get(0).getCohortName());
  }
}