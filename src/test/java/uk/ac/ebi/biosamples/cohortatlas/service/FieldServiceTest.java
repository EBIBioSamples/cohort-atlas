package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldSearchRepository;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FieldServiceTest {
  @Mock
  FieldRepository fieldRepository;
  @Mock
  FieldSearchRepository fieldSearchRepository;

  @InjectMocks
  FieldService fieldService;


  @Test
  public void search_should_return_a_list_of_fields() {
    Pageable pageRequest = PageRequest.of(0, 10);
    String text = "";
    List<String> filters = Collections.emptyList();
    String sort = "";

    Page<DictionaryField> fieldPage = new PageImpl<>(getTestFields(), pageRequest, getTestFields().size());
    Mockito.when(fieldSearchRepository.findFieldPageWithFilters(pageRequest, text, sort, filters)).thenReturn(fieldPage);

    Page<DictionaryField> fields = fieldService.searchFields(pageRequest, text, filters, sort);
    Assertions.assertNotNull(fields);
    Assertions.assertEquals(2, fields.getTotalElements());
  }

  private List<DictionaryField> getTestFields() {
    return List.of(new DictionaryField(), new DictionaryField());
  }

}
