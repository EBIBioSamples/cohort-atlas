package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortModelAssembler;
import uk.ac.ebi.biosamples.cohortatlas.service.CohortService;

import java.util.InputMismatchException;
import java.util.List;

@RestController
@RequestMapping(value = "/cohorts", produces = {"application/hal+json"})
public class CohortController {
  private final CohortService cohortService;
  private final CohortModelAssembler cohortModelAssembler;

  public CohortController(CohortService cohortService, CohortModelAssembler cohortModelAssembler) {
    this.cohortService = cohortService;
    this.cohortModelAssembler = cohortModelAssembler;
  }

  @GetMapping()
  public ResponseEntity<PagedModel<EntityModel<Cohort>>> searchCohorts(@RequestParam(required = false) Integer page,
                                                                       @RequestParam(required = false) Integer size,
                                                                       @RequestParam(required = false) String text,
                                                                       @RequestParam(value = "filter", required = false) List<String> filters,
                                                                       @RequestParam(required = false) String sort) {
    Pageable pageRequest = cohortService.getPageRequest(page, size);
    Page<Cohort> cohortPage = cohortService.searchCohorts(pageRequest, text, filters, sort);
    return ResponseEntity.ok(cohortModelAssembler.toPagedModel(cohortPage));
  }

  @PostMapping()
  public ResponseEntity<EntityModel<Cohort>> createCohort(@RequestBody Cohort cohort) {
    //todo generate accession
    Cohort savedCohort = cohortService.saveCohort(cohort);
    EntityModel<Cohort> cohortModel = cohortModelAssembler.toModel(savedCohort);

    return ResponseEntity.created(cohortModel.getLink("self")
            .orElseThrow(() -> new RuntimeException("failed to create model")).toUri())
        .body(cohortModel);
  }

  @GetMapping("/{accession}")
  public ResponseEntity<EntityModel<Cohort>> getCohort(@PathVariable String accession) {
    Cohort cohort = cohortService.getCohortById(accession);
    return ResponseEntity.ok(cohortModelAssembler.toModel(cohort));
  }

  @PutMapping("/{accession}")
  public ResponseEntity<EntityModel<Cohort>> saveCohort(@PathVariable String accession, @RequestBody Cohort cohort) {
    if (!accession.equals(cohort.getCohortId())) {
      throw new InputMismatchException("Accession mismatch");
    }

    Cohort savedCohort = cohortService.saveCohort(cohort);
    return ResponseEntity.ok(cohortModelAssembler.toModel(savedCohort));
  }

  @DeleteMapping("/{accession}")
  public ResponseEntity<Void> deleteCohort(@PathVariable String accession) {
    cohortService.deleteCohort(accession);
    return ResponseEntity.noContent().build();
  }
}
