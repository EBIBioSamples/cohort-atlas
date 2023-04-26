package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.AccessionSequence;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AccessionService {
    MongoOperations mongoOperations;

    @Autowired
    public AccessionService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public String generateAccession(String seqName) {

        AccessionSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                AccessionSequence.class);
        long nextVal = !Objects.isNull(counter) ? counter.getSeq() : 1;
        return "BSC" + String.format("%06d", nextVal);
    }
}
