package uk.ac.ebi.biosamples.cohortatlas.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;

import java.util.List;

@RequiredArgsConstructor
public class RelationshipSearchRepository implements SearchRepository<Relationship> {
  private final SearchQueryHelper searchQueryHelper = new SearchQueryHelper();
  protected MongoTemplate mongoTemplate;

  public Page<Relationship> findByFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = searchQueryHelper.getSearchQuery(page, text, sort, filters);
    List<Relationship> relationships = mongoTemplate.find(query, Relationship.class);

    return PageableExecutionUtils.getPage(relationships, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Relationship.class));
  }
}
