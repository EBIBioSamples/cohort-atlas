package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;

public interface RelationshipRepository extends MongoRepository<Relationship, String>, SearchRepository<Relationship> {

}
