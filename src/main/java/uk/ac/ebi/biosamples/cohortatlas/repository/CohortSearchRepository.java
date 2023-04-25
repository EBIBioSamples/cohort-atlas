package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

@Repository
public class CohortSearchRepository extends SearchRepository {

  public CohortSearchRepository(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public Page<Cohort> findPageWithFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = getSearchQuery(page, text, sort, filters);
    List<Cohort> cohorts = mongoTemplate.find(query, Cohort.class);

    return PageableExecutionUtils.getPage(cohorts, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Cohort.class));
  }
}
