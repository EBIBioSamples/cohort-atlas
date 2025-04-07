package uk.ac.ebi.biosamples.cohortatlas.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.biosamples.cohortatlas.repository.SearchRepository;

import java.util.List;

@Repository
public class ProjectSearchRepository extends SearchRepository {
  public ProjectSearchRepository(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public Page<Project> findFieldPageWithFilters(Pageable page, String text, String sort, List<String> filters) {
    Query query = getSearchQuery(page, text, sort, filters);
    List<Project> projects = mongoTemplate.find(query, Project.class);

    return PageableExecutionUtils.getPage(projects, page,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Project.class));
  }
}
