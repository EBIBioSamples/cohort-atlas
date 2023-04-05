package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.List;

@RestController
public class CohortController {
  private final CohortService cohortService;

  public CohortController(CohortService cohortService) {
    this.cohortService = cohortService;
  }

  @GetMapping("cohorts")
  public List<Cohort> getCohorts() {
    return cohortService.searchCohorts();
  }
}
