package uk.ac.ebi.biosamples.cohortatlas.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

@RequiredArgsConstructor
public class CohortSearchRepository implements SearchRepository <Cohort>{

  private final SearchQueryHelper searchQueryHelper = new SearchQueryHelper();
  protected MongoTemplate mongoTemplate;

  public Page<Cohort> findByFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = searchQueryHelper.getSearchQuery(page, text, sort, filters);
    List<Cohort> cohorts = mongoTemplate.find(query, Cohort.class);

    return PageableExecutionUtils.getPage(cohorts, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Cohort.class));
  }
}
