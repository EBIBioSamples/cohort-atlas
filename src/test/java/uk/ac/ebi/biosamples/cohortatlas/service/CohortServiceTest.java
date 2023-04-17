package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.SearchRepository;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CohortServiceTest {
  @Mock
  CohortRepository cohortRepository;
  @Mock
  SearchRepository searchRepository;

  @InjectMocks
  CohortService cohortService;

  @Test
  public void search_should_return_a_list_of_cohorts() {
    Pageable pageRequest = PageRequest.of(0, 10);
    String text = "";
    List<String> filters = Collections.emptyList();
    String sort = "";

    Page<Cohort> cohortPage = new PageImpl<>(getTestCohorts(), pageRequest, getTestCohorts().size());
    Mockito.when(searchRepository.findPageWithFilters(pageRequest, text, sort, filters)).thenReturn(cohortPage);

    Page<Cohort> cohorts = cohortService.searchCohorts(pageRequest, text, filters, sort);
    Assertions.assertNotNull(cohorts);
    Assertions.assertEquals(3, cohorts.getTotalElements());
  }

  @Test
  public void jsr310InstantSerializationTest() {
    Instant instant = Instant.ofEpochMilli(1681760766018L);
    String instantString = instant.toString();
    Assertions.assertEquals(instantString, "2023-04-17T19:46:06.018Z");
  }

  private List<Cohort> getTestCohorts() {
    return List.of(new Cohort(), new Cohort(), new Cohort());
  }

}
