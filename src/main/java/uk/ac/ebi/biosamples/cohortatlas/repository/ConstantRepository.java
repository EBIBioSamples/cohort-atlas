package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.biosamples.cohortatlas.model.Constant;

import java.util.List;

public interface ConstantRepository extends MongoRepository<Constant, String> {
}
