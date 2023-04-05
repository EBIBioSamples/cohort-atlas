package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CohortServiceTest {

  @Mock
  CohortRepository cohortRepository;

  @InjectMocks
  CohortService cohortService;

  @Test
  public void search_should_return_a_list_of_cohorts() {
    Mockito.when(cohortRepository.findAll()).thenReturn(getTestCohorts());

    List<Cohort> cohorts = cohortService.searchCohorts();
    Assertions.assertNotNull(cohorts);
    Assertions.assertEquals(3, cohorts.size());
  }

  private List<Cohort> getTestCohorts() {
    return List.of(new Cohort(), new Cohort(), new Cohort());
  }

}
