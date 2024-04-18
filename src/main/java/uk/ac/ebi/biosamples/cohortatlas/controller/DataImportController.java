package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.service.DataImportService;

@RestController
@RequestMapping(value = "/import")
public class DataImportController {
  private final DataImportService dataImportService;

  public DataImportController(DataImportService dataImportService) {
    this.dataImportService = dataImportService;
  }

  @PostMapping(value = "/cohorts")
  public ResponseEntity<Void> importCohorts() {
    dataImportService.importCohorts();
    return ResponseEntity.ok().build();
  }
}
