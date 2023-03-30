package uk.ac.ebi.biosamples.cohortatlas;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

@Service
public class CohortService {
  private CohortRepository cohortRepository;

  public CohortService(CohortRepository cohortRepository) {
    this.cohortRepository = cohortRepository;
  }

  public List<Cohort> searchCohorts() {
    return cohortRepository.findAll();
  }
}
