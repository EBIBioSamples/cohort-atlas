package uk.ac.ebi.biosamples.cohortatlas.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.model.DataTypes;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
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

  //TODO: test here
  @Test
  void filterCohorts() {
    Cohort cohort = new Cohort();
    cohort.setCohortName("first");
    cohort.setDescription("first");
    cohort.setAcronym("filter1");
    Facet facet = new Facet();
    facet.setDataTypes(Arrays.asList("biospecimens"));
    facet.setLicences(Collections.singletonList("MIT"));
    cohort.setFacet(facet);
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("second");
    cohort.setDescription("second");
    facet = new Facet();
    facet.setDataTypes(Arrays.asList("phenotypicData"));
    facet.setLicences(Collections.singletonList("MIT"));
    cohort.setFacet(facet);
    cohort.setAcronym("filter2");
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("third");
    cohort.setDescription("third");
    facet = new Facet();
    facet.setDataTypes(Arrays.asList("biospecimens","genomicData"));
    facet.setLicences(Collections.singletonList("Apache"));
    cohort.setFacet(facet);
    cohort.setAcronym("filter3");
    cohortService.saveCohort(cohort);

    cohort = new Cohort();
    cohort.setCohortName("fourth");
    cohort.setDescription("fourth");
    cohort.setAcronym("filter4");
    cohortService.saveCohort(cohort);

    //single filter
    Page<Cohort> page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
            "cohortName", Collections.singletonList("dataTypes:biospecimens"));
    page.forEach( cohort1 -> assertTrue( cohort1.getFacet().getDataTypes().contains("biospecimens")));

    //single filter multiple values(OR one of the values must be present as it si single category)
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
            "cohortName", Collections.singletonList("dataTypes:biospecimens~genomicData"));
    page.forEach( cohort1 -> assertTrue( (cohort1.getFacet().getDataTypes().contains("biospecimens")
            || cohort1.getFacet().getDataTypes().contains("genomicData")
    )));

    //multiple filter single values, when there are multiple categories it should be AND(both to be present)
    page = cohortSearchRepository.findPageWithFilters(PageRequest.of(0, 5), null,
            "cohortName", Arrays.asList("dataTypes:phenotypicData","licences:MIT"));
    page.forEach( cohort1 -> assertTrue( (cohort1.getFacet().getDataTypes().contains("phenotypicData")
            && cohort1.getFacet().getLicences().contains("MIT")
    )));

  }


}