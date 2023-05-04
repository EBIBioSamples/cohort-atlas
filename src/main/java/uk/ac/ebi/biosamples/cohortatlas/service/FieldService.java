package uk.ac.ebi.biosamples.cohortatlas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldSearchRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FieldService {
  private final FieldRepository fieldRepository;
  private final FieldSearchRepository fieldSearchRepository;

  public List<DictionaryField> searchFields(String cohort) {
    return fieldRepository.findByCohort(cohort);
  }

  public Page<DictionaryField> searchFields(Pageable pageRequest, String text, List<String> filters, String sort) {
    return fieldSearchRepository.findByFilters(pageRequest, text, sort, filters);
  }

  public List<Map<String, Integer>> fieldCountGroupByCohort() {
    return fieldRepository.getFieldCountGroupByCohort().getMappedResults();
  }

}
