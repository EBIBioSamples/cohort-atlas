package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetResult;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetSummary;
import uk.ac.ebi.biosamples.cohortatlas.service.FacetService;

import java.util.List;

@RestController
@RequestMapping(value = "/facets", produces = {"application/hal+json"})
public class FacetController {
  private final FacetService facetService;

  public FacetController(FacetService facetService) {
    this.facetService = facetService;
  }

  @GetMapping()
  public ResponseEntity<List<FacetResult>> getFacets() {
    List<FacetResult> facets = facetService.getFacets();
    return ResponseEntity.ok(facets);
  }

  @GetMapping("/summary")
  public ResponseEntity<FacetSummary> getFacetSummary() {
    FacetSummary summary = facetService.getHighLevelSummary();
    return ResponseEntity.ok(summary);
  }

}
