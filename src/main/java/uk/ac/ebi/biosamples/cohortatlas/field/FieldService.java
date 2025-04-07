package uk.ac.ebi.biosamples.cohortatlas.field;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.util.List;
import java.util.Map;

@Service
public class FieldService {
  private final FieldRepository fieldRepository;
  private final FieldSearchRepository fieldSearchRepository;

  public FieldService(FieldRepository fieldRepository, FieldSearchRepository fieldSearchRepository) {
    this.fieldRepository = fieldRepository;
    this.fieldSearchRepository = fieldSearchRepository;
  }

  public List<DictionaryField> searchFields(String cohort) {
    return fieldRepository.findByCohort(cohort);
  }

  public Page<DictionaryField> searchFields(Pageable pageRequest, String text, List<String> filters, String sort) {
    return fieldSearchRepository.findFieldPageWithFilters(pageRequest, text, sort, filters);
  }

  public List<Map<String, Integer>> fieldCountGroupByCohort() {
    return fieldRepository.getFieldCountGroupByCohort().getMappedResults();
  }

  public List<DictionaryField> saveFields(List<Field> fields, String cohort) {
    List<DictionaryField> dictionaryFields = fields.stream().map(f -> convertFieldToDictionaryField(f, cohort)).toList();
    return fieldRepository.saveAll(dictionaryFields);
  }

  private static DictionaryField convertFieldToDictionaryField(Field field, String cohort) {
    DictionaryField dictionaryField = DictionaryField.from(field);
    dictionaryField.setCohort(cohort);
    return dictionaryField;
  }

}
