package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Project;
import uk.ac.ebi.biosamples.cohortatlas.repository.ProjectRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.ProjectSearchRepository;

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
