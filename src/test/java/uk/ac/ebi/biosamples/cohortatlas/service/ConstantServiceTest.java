package uk.ac.ebi.biosamples.cohortatlas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.ebi.biosamples.cohortatlas.model.Constant;
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
        Constant constant = new Constant();
        constant.setId("100");
        constant.setType("region");
        constant.setValue("APAC");
        Constant saved =constantService.saveConstant(constant);

        constant = new Constant();
        constant.setId("101");
        constant.setType("region");
        constant.setValue("EU");
        constantService.saveConstant(constant);

        constantService.findAllFilters();
    }

}