package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.repository.DictionaryFieldRepository;
import uk.ac.ebi.biosamples.cohortatlas.service.DictionaryFieldService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DictionaryFieldServiceTest {

  @Mock
  DictionaryFieldRepository dictionaryFieldRepository;

  @InjectMocks
  DictionaryFieldService dictionaryFieldService;


  @Test
  public void search_should_return_a_list_of_fields() {
    Mockito.when(dictionaryFieldRepository.findAll()).thenReturn(getTestFields());

    List<DictionaryField> fields = dictionaryFieldService.searchFields();
    Assertions.assertNotNull(fields);
    Assertions.assertEquals(2, fields.size());
  }

  private List<DictionaryField> getTestFields() {
    return List.of(new DictionaryField(), new DictionaryField());
  }

}
