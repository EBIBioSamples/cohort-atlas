package uk.ac.ebi.biosamples.cohortatlas.cohort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CohortRepository extends MongoRepository<Cohort, String> {
  Page<Cohort> findAllBy(TextCriteria criteria, Pageable pageable);
}
