package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.service.FieldService;
import uk.ac.ebi.biosamples.cohortatlas.service.FieldModelAssembler;
import uk.ac.ebi.biosamples.cohortatlas.utils.PageUtils;

import java.util.List;
import java.util.Map;

@RestController
public class FieldController {
  private final FieldService fieldService;
  private final FieldModelAssembler fieldModelAssembler;

  public FieldController(FieldService fieldService, FieldModelAssembler fieldModelAssembler) {
    this.fieldService = fieldService;
    this.fieldModelAssembler = fieldModelAssembler;
  }

  @GetMapping("/fields/all")
  public List<DictionaryField> getFields(@RequestParam(required = false) String accession) {
    return fieldService.searchFields(accession);
  }

  @GetMapping("/fields")
  public ResponseEntity<PagedModel<EntityModel<DictionaryField>>> searchFields(@RequestParam(required = false) Integer page,
                                                                              @RequestParam(required = false) Integer size,
                                                                              @RequestParam(required = false) String text,
                                                                              @RequestParam(value = "filter", required = false) List<String> filters,
                                                                              @RequestParam(required = false) String sort) {
    Pageable pageRequest = PageUtils.getPageRequest(page, size);
    Page<DictionaryField> fieldPage = fieldService.searchFields(pageRequest, text, filters, sort);
    return ResponseEntity.ok(fieldModelAssembler.toPagedModel(fieldPage));
  }

  @GetMapping("/fields/cohorts")
  public ResponseEntity<List<Map<String, Integer>>> getFieldCountForCohorts() {
    return ResponseEntity.ok(fieldService.fieldCountGroupByCohort());
  }
}
