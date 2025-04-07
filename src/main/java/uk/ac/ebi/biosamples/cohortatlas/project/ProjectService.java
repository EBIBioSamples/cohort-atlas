package uk.ac.ebi.biosamples.cohortatlas.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectSearchRepository projectSearchRepository;

  public ProjectService(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
    this.projectRepository = projectRepository;
    this.projectSearchRepository = projectSearchRepository;
  }

  public Page<Project> searchProjects(Pageable pageRequest, String text, List<String> filters, String sort) {
    return projectSearchRepository.findFieldPageWithFilters(pageRequest, text, sort, filters);
  }

  public List<Map<String, Integer>> projectCountGroupByCohort() {
    return projectRepository.getFieldCountGroupByCohort().getMappedResults();
  }

}
