package uk.ac.ebi.biosamples.cohortatlas.field;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;
import uk.ac.ebi.biosamples.cohortatlas.service.HarmonisationService;
import uk.ac.ebi.biosamples.cohortatlas.utils.PageUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FieldController {
  private final FieldService fieldService;
  private final FieldModelAssembler fieldModelAssembler;
  private final HarmonisationService harmonisationService;

  public FieldController(FieldService fieldService, FieldModelAssembler fieldModelAssembler, HarmonisationService harmonisationService) {
    this.fieldService = fieldService;
    this.fieldModelAssembler = fieldModelAssembler;
    this.harmonisationService = harmonisationService;
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

  @PutMapping("/dictionary/harmonise")
  public ResponseEntity<List<Field>> harmoniseCohortDictionary(@RequestParam("file") MultipartFile file) {
    try {
      return ResponseEntity.ok(harmonisationService.harmoniseDictionary("", extractFields(file)));
    } catch (IOException e) {
      log.error("Failed to process file. IO Error {}", e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
  }

  private List<Field> extractFields(MultipartFile file) throws IOException {
    CsvMapper mapper = new CsvMapper();
    CsvSchema csvSchema = mapper
        .typedSchemaFor(Field.class)
        .withHeader()
        .withColumnSeparator(',')
        .withComments();

    MappingIterator<Field> csvIterator = mapper.readerWithTypedSchemaFor(Field.class).with(csvSchema)
        .readValues(file.getInputStream());
    return csvIterator.readAll();
  }
}
