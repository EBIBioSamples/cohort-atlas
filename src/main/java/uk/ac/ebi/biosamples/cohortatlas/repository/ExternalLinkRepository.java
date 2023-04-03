package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;

public interface ExternalLinkRepository extends MongoRepository<DictionaryField, String> {

}
