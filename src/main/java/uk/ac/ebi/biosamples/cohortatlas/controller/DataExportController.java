package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

@RestController
@RequestMapping(value = "/export")
public class DataExportController {
  private CohortController cohortController;

  public DataExportController(CohortController cohortController) {
    this.cohortController = cohortController;
  }

  @RequestMapping(value = "/cohorts", method = RequestMethod.GET, produces = "application/xml")
  public ResponseEntity<PagedModel<EntityModel<Cohort>>> exportCohorts(@RequestParam(defaultValue = "EBI_SEARCH") String format,
                                                                       @RequestParam(required = false) Integer page,
                                                                       @RequestParam(required = false) Integer size,
                                                                       @RequestParam(required = false) String text,
                                                                       @RequestParam(value = "filter", required = false) List<String> filters,
                                                                       @RequestParam(required = false) String sort) {

    ResponseEntity<PagedModel<EntityModel<Cohort>>> cohortsModel = cohortController.searchCohorts(page, size, text, filters, sort);
    if (format.equalsIgnoreCase("EBI_SEARCH")) {
//      List<Cohort> cohot
    }

    return cohortsModel;
  }
}
