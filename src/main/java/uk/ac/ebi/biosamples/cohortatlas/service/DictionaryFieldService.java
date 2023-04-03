package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.repository.DictionaryFieldRepository;

import java.util.List;

@Service
public class DictionaryFieldService {
  private final DictionaryFieldRepository dictionaryFieldRepository;

  public DictionaryFieldService(DictionaryFieldRepository dictionaryFieldRepository) {
    this.dictionaryFieldRepository = dictionaryFieldRepository;
  }

  public List<DictionaryField> searchFields() {
    return dictionaryFieldRepository.findAll();
  }
}
