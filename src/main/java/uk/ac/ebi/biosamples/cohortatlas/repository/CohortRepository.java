package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

@Repository
public interface CohortRepository extends MongoRepository<Cohort, String>, SearchRepository {
  Page<Cohort> findAllBy(TextCriteria criteria, Pageable pageable);
}
