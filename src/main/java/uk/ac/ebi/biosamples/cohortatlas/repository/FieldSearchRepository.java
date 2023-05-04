package uk.ac.ebi.biosamples.cohortatlas.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;

import java.util.List;

@RequiredArgsConstructor
public class FieldSearchRepository implements SearchRepository<DictionaryField> {
  protected MongoTemplate mongoTemplate;
  private final SearchQueryHelper searchQueryHelper = new SearchQueryHelper();

  public Page<DictionaryField> findByFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = searchQueryHelper.getSearchQuery(page, text, sort, filters);
    List<DictionaryField> cohorts = mongoTemplate.find(query, DictionaryField.class);

    return PageableExecutionUtils.getPage(cohorts, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), DictionaryField.class));
  }
}
