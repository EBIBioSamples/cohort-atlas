package uk.ac.ebi.biosamples.cohortatlas.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.Arrays;
import java.util.Collections;


@SpringBootTest
@ActiveProfiles("test")
class CohortSearchRepositoryIntegrationTest {
  @Autowired
  private CohortService cohortService;
  @Autowired
  private CohortSearchRepository cohortSearchRepository;


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

    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), "CohortSearchRepositoryIntegrationTest", "cohortName", Collections.emptyList());
    assertTrue(page.getSize() > 1);
    assertEquals("SearchRepositoryIntegrationTest_freeTextSearch_1", page.getContent().get(0).getCohortName());
  }

  @Test
  void filterCohorts() {
    Cohort cohort = new Cohort();
    cohort.setCohortName("first");
    cohort.setDescription("first");
    cohort.setAcronym("filter1");
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("second");
    cohort.setDescription("second");
    cohort.setAcronym("filter2");
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("third");
    cohort.setDescription("third");
    cohort.setAcronym("filter3");
    cohortService.saveCohort(cohort);

    //single filter
    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
            "cohortName", Collections.singletonList("acronym:filter3"));
    assertEquals(1, page.getContent().size());
    assertEquals("third", page.getContent().get(0).getCohortName());

    //single field multiple filter
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
            Collections.singletonList("acronym:filter3~filter2"));
    assertEquals(2, page.getContent().size());

    assertTrue((page.getContent().get(0).getCohortName().equals("second")
            && page.getContent().get(1).getCohortName().equals("third"))
    || (page.getContent().get(0).getCohortName().equals("third")
            && page.getContent().get(0).getCohortName().equals("second")));


    //multiple field multiple filter, second filter does not match with the first filter
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
            Arrays.asList("acronym:filter3~filter2","description:first"));
    assertEquals(0, page.getContent().size());

    //multiple field multiple filter. Only third cohort matches both acronym and description
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
            Arrays.asList("acronym:filter3~filter2","description:third"));
    assertEquals(1, page.getContent().size());
    assertEquals("third", page.getContent().get(0).getCohortName());

  }


}