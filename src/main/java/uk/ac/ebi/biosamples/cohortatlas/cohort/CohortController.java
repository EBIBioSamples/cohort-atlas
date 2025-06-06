package uk.ac.ebi.biosamples.cohortatlas.cohort;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.biosamples.cohortatlas.field.FieldService;
import uk.ac.ebi.biosamples.cohortatlas.harmonisation.HarmonisationService;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;
import uk.ac.ebi.biosamples.cohortatlas.model.Relation;
import uk.ac.ebi.biosamples.cohortatlas.model.Survey;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

@RestController
@RequestMapping(value = "/cohorts", produces = {"application/hal+json"})
public class CohortController {
  private final CohortService cohortService;
  private final HarmonisationService harmonisationService;
  private final CohortModelAssembler cohortModelAssembler;
  private final FieldService fieldService;

  public CohortController(CohortService cohortService,
                          @Qualifier("geminiHarmonisationService") HarmonisationService harmonisationService,
                          CohortModelAssembler cohortModelAssembler,
                          FieldService fieldService) {
    this.cohortService = cohortService;
    this.harmonisationService = harmonisationService;
    this.cohortModelAssembler = cohortModelAssembler;
    this.fieldService = fieldService;
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
    if (!accession.equals(cohort.getAccession())) {
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

  @PatchMapping("/{accession}")
  public ResponseEntity<Void> patchCohort(@PathVariable String accession, @RequestBody Cohort cohort) {
    Cohort savedCohort = cohortService.getCohortById(accession);
    //todo add bsd, ega links
    // add survey questions?
    savedCohort.setRelationships(cohort.getRelationships());
    cohortService.saveCohort(savedCohort);
    return  ResponseEntity.noContent().build();
  }

  @PutMapping("/{accession}/fields")
  public ResponseEntity<Void> saveDictionary(@PathVariable String accession, @RequestBody List<Field> fields) {
    cohortService.saveDictionaryFields(accession, fields);
    return ResponseEntity.accepted().build();
  }

  @PutMapping("/{accession}/relationships")
  public ResponseEntity<Cohort> saveRelationships(@PathVariable String accession,
                                                   @RequestBody List<Relation> relationships) {
    Cohort cohort = cohortService.getCohortById(accession);
    cohort.setRelationships(relationships);
    cohortService.saveCohort(cohort);
    return ResponseEntity.ok(cohort);
  }

  @PutMapping("/{accession}/survey")
  public ResponseEntity<Cohort> saveSurveyData(@PathVariable String accession, @RequestBody Survey survey) {
    Cohort cohort = cohortService.getCohortById(accession);
//    cohort.setSurvey(survey);
    cohortService.saveCohort(cohort);
    return ResponseEntity.ok(cohort);
  }

  @PostMapping("/{accession}/dictionary")
  public ResponseEntity<List<Field>> handleDictionaryFile(@PathVariable String accession,
                                                   @RequestParam("file") MultipartFile file) throws IOException {
    cohortService.saveDictionaryFields(accession, file);
    List<Field> dictionary = triggerHarmonisation(accession);
    cohortService.saveDictionaryFields(accession, dictionary);
    fieldService.saveFields(dictionary, accession);
    return ResponseEntity.ok(dictionary);
  }

  @PutMapping("/{accession}/dictionary/harmonise")
  public ResponseEntity<Void> harmoniseCohortDictionary(@PathVariable String accession) {
    triggerHarmonisation(accession);
    return ResponseEntity.accepted().build();
  }

  private List<Field> triggerHarmonisation(String accession) {
    Cohort cohort = cohortService.getCohortById(accession);
    return harmonisationService.harmoniseDictionary(accession, cohort.getDictionary());
  }
}
