package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.model.Constant;
import uk.ac.ebi.biosamples.cohortatlas.service.ConstantService;

import java.util.List;
import java.util.Map;

@RestController
public class ConstantController {
  private final ConstantService constantService;

  public ConstantController(ConstantService constantService) {
    this.constantService = constantService;
  }

  @GetMapping("/filters")
  public ResponseEntity<Map<String,List<String>>> searchRelationships(@RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer size,
                                                                      @RequestParam(required = false) String text,
                                                                      @RequestParam(value = "filter", required = false) List<String> filters,
                                                                      @RequestParam(required = false) String sort) {

    Map<String,List<String>> all = constantService.findAllFilters();
    return ResponseEntity.ok(all);
  }
}
