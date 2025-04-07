package uk.ac.ebi.biosamples.cohortatlas.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.cohortatlas.utils.PageUtils;

import java.util.List;

@RestController
public class ProjectController {
  private final ProjectService projectService;
  private final ProjectModelAssembler projectModelAssembler;

  public ProjectController(ProjectService projectService, ProjectModelAssembler projectModelAssembler) {
    this.projectService = projectService;
    this.projectModelAssembler = projectModelAssembler;
  }

  @GetMapping("/projects")
  public ResponseEntity<PagedModel<EntityModel<Project>>> searchProjects(@RequestParam(required = false) Integer page,
                                                                         @RequestParam(required = false) Integer size,
                                                                         @RequestParam(required = false) String text,
                                                                         @RequestParam(value = "filter", required = false) List<String> filters,
                                                                         @RequestParam(required = false) String sort) {
    Pageable pageRequest = PageUtils.getPageRequest(page, size);
    Page<Project> projectPage = projectService.searchProjects(pageRequest, text, filters, sort);
    return ResponseEntity.ok(projectModelAssembler.toPagedModel(projectPage));
  }

//  @GetMapping("/fields/cohorts")
//  public ResponseEntity<List<Map<String, Integer>>> getFieldCountForCohorts() {
//    return ResponseEntity.ok(projectService.projectCountGroupByCohort());
//  }
}
