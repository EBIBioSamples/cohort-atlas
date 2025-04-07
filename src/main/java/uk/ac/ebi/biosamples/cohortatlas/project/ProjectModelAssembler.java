package uk.ac.ebi.biosamples.cohortatlas.project;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import uk.ac.ebi.biosamples.cohortatlas.field.FieldController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ProjectModelAssembler implements RepresentationModelAssembler<Project, EntityModel<Project>> {

  private final PagedResourcesAssembler<Project> projectResourcesAssembler;

  public ProjectModelAssembler(PagedResourcesAssembler<Project> projectResourcesAssembler) {
    this.projectResourcesAssembler = projectResourcesAssembler;
  }

  @Override
  public EntityModel<Project> toModel(@NonNull Project project) {
    Link link = linkTo(FieldController.class).slash(project.getAccession()).withSelfRel();
    return EntityModel.of(project, link);
  }

  @Override
  public CollectionModel<EntityModel<Project>> toCollectionModel(Iterable<? extends Project> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }

  public PagedModel<EntityModel<Project>> toPagedModel(Page<Project> projectPage) {
    Link link = linkTo(ProjectController.class).withSelfRel();
    return projectResourcesAssembler.toModel(projectPage, this, link);
  }
}
