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
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;
import uk.ac.ebi.biosamples.cohortatlas.service.FieldService;
import uk.ac.ebi.biosamples.cohortatlas.service.FieldModelAssembler;
import uk.ac.ebi.biosamples.cohortatlas.service.RelationshipModelAssembler;
import uk.ac.ebi.biosamples.cohortatlas.service.RelationshipService;
import uk.ac.ebi.biosamples.cohortatlas.utils.PageUtils;

import java.util.List;

@RestController
public class RelationshipController {
  private final RelationshipService relationshipService;
  private final RelationshipModelAssembler relationshipModelAssembler;

  public RelationshipController(RelationshipService relationshipService, RelationshipModelAssembler relationshipModelAssembler) {
    this.relationshipService = relationshipService;
    this.relationshipModelAssembler = relationshipModelAssembler;
  }

  @GetMapping("/relationships")
  public ResponseEntity<PagedModel<EntityModel<Relationship>>> searchRelationships(@RequestParam(required = false) Integer page,
                                                                                   @RequestParam(required = false) Integer size,
                                                                                   @RequestParam(required = false) String text,
                                                                                   @RequestParam(value = "filter", required = false) List<String> filters,
                                                                                   @RequestParam(required = false) String sort) {
    Pageable pageRequest = PageUtils.getPageRequest(page, size);
    Page<Relationship> fieldPage = relationshipService.searchRelationships(pageRequest, text, filters, sort);
    return ResponseEntity.ok(relationshipModelAssembler.toPagedModel(fieldPage));
  }
}
