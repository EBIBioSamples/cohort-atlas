package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;

import java.util.List;

@Repository
public class RelationshipSearchRepository extends CohortSearchRepository {
  public RelationshipSearchRepository(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public Page<Relationship> findFieldPageWithFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = getSearchQuery(page, text, sort, filters);
    List<Relationship> relationships = mongoTemplate.find(query, Relationship.class);

    return PageableExecutionUtils.getPage(relationships, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Relationship.class));
  }
}
