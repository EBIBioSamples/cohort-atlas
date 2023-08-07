package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.ebi.biosamples.cohortatlas.repository.ConstantRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.FieldSearchRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConstantServiceTest {
    @Mock
    ConstantRepository constantRepository;

    @InjectMocks
    ConstantService constantService;

    @Test
    public void findAllFilterConstants() {
        constantService.findAllFilters();
    }

}