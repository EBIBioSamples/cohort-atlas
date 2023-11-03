package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import uk.ac.ebi.biosamples.cohortatlas.CohortAtlasIntegrationTest;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CohortSearchRepositoryIntegrationTest extends CohortAtlasIntegrationTest {
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

    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(
        PageRequest.of(0, 5), "CohortSearchRepositoryIntegrationTest",
        "cohortName", Collections.emptyList());
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
    cohort.setDataTypes(Collections.singletonList("testDataType"));
    cohortService.saveCohort(cohort);

    //single filter
    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
        "cohortName", Collections.singletonList("dataTypes:biospecimens"));
    int pageSizeBefore = page.getContent().size();
    page.forEach(cohort1 -> assertTrue(cohort1.getDataTypes().contains("biospecimens")));

    cohort = new Cohort();
    cohort.setCohortName("third");
    cohort.setDescription("third");
    cohort.setAcronym("filter3");
    List<String> dataTypes = List.of("biospecimens", "testDataType");
    cohort.setDataTypes(dataTypes);
    cohortService.saveCohort(cohort);

    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
        "accession", Collections.singletonList("dataTypes:biospecimens"));
    assertEquals(1 + pageSizeBefore, page.getContent().size());
    page.forEach(cohort1 -> assertTrue(cohort1.getDataTypes().contains("biospecimens")));
    assertEquals("third", page.getContent().get(pageSizeBefore).getCohortName());

    //single filter
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
        "cohortName", Collections.singletonList("acronym:filter3"));
    assertEquals(1, page.getContent().size());
    assertEquals("third", page.getContent().get(0).getCohortName());

    //single field multiple filter
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
        List.of("acronym:filter2", "dataTypes:testDataType"));
    assertEquals(1, page.getContent().size());

    assertEquals("second", page.getContent().get(0).getCohortName());

    //multiple field multiple filter, second filter does not match with the first filter
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
        Arrays.asList("acronym:filter3", "description:first"));
    assertEquals(0, page.getContent().size());

    //multiple field multiple filter. Only third cohort matches both acronym and description
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null, "cohortName",
        Arrays.asList("dataTypes:testDataType", "description:third"));
    assertEquals(1, page.getContent().size());
    assertEquals("third", page.getContent().get(0).getCohortName());

  }
}