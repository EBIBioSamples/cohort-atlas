package uk.ac.ebi.biosamples.cohortatlas.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import uk.ac.ebi.biosamples.cohortatlas.model.AccessionSequence;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.service.AccessionService;

@Component
public class CohortListener  extends AbstractMongoEventListener<Cohort> {

    private AccessionService accessionService;

    @Autowired
    public CohortListener(AccessionService accessionService) {
        this.accessionService = accessionService;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Cohort> event) {
        if (event.getSource().getAccession() == null) {
            event.getSource().setAccession(accessionService.generateAccession(Cohort.SEQUENCE_NAME));
        }
    }
}
