package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

@Repository
public class SearchRepository {
  private final MongoTemplate mongoTemplate;

  public SearchRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Page<Cohort> findPageWithFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = getSearchQuery(page, text, sort, filters);
    List<Cohort> cohorts = mongoTemplate.find(query, Cohort.class);

    return PageableExecutionUtils.getPage(cohorts, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Cohort.class));
  }

  private Query getSearchQuery(Pageable page, String text, String sort, List<String> filters) {
    Query query = new Query().with(page);
    populateQueryWithSort(query, sort);
    populateQueryWithFreeTextSearch(query, text);
    populateQueryWithFilters(query, filters);
    return query;
  }

  private void populateQueryWithSort(Query query, String sort) {
    if (StringUtils.hasText(sort)) {
      query.with(Sort.by(sort).ascending());
    }
  }

  private void populateQueryWithFreeTextSearch(Query query, String text) {
    if (StringUtils.hasText(text)) {
      query.addCriteria(TextCriteria.forDefaultLanguage().matchingAny(text));
    }
  }

  private void populateQueryWithFilters(Query query, List<String> filters) {
    if (CollectionUtils.isEmpty(filters)) {
      return;
    }

    for (String filter : filters) {
      String[] filterParts = filter.split(":");
      if (filterParts.length > 1) {
        String field = filterParts[0];
        String value = filterParts[1];
        query.addCriteria(Criteria.where(field).is(value));
      }
    }
  }

}
