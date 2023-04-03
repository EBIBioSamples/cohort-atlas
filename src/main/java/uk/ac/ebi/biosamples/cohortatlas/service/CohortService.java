package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;

import java.util.List;

@Service
public class CohortService {
  private final CohortRepository cohortRepository;

  public CohortService(CohortRepository cohortRepository) {
    this.cohortRepository = cohortRepository;
  }

  public List<Cohort> searchCohorts() {
    return cohortRepository.findAll();
  }
}
