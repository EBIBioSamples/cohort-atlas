package uk.ac.ebi.biosamples.cohortatlas;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

public interface CohortRepository extends MongoRepository<Cohort, String> {

}
