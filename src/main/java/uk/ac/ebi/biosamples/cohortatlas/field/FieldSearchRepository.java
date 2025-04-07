package uk.ac.ebi.biosamples.cohortatlas.field;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.repository.SearchRepository;

import java.util.List;

@Repository
public class FieldSearchRepository extends SearchRepository {
  public FieldSearchRepository(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public Page<DictionaryField> findFieldPageWithFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = getSearchQuery(page, text, sort, filters);
    List<DictionaryField> cohorts = mongoTemplate.find(query, DictionaryField.class);

    return PageableExecutionUtils.getPage(cohorts, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), DictionaryField.class));
  }
}
