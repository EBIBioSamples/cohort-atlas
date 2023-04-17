package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import uk.ac.ebi.biosamples.cohortatlas.controller.CohortController;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CohortModelAssembler implements RepresentationModelAssembler<Cohort, EntityModel<Cohort>> {

  private final PagedResourcesAssembler<Cohort> cohortPageAssembler;

  public CohortModelAssembler(PagedResourcesAssembler<Cohort> cohortPageAssembler) {
    this.cohortPageAssembler = cohortPageAssembler;
  }

  @Override
  public EntityModel<Cohort> toModel(@NonNull Cohort cohort) {
    Link link = linkTo(CohortController.class).slash(cohort.getCohortId()).withSelfRel();
    return EntityModel.of(cohort, link);
  }

  @Override
  public CollectionModel<EntityModel<Cohort>> toCollectionModel(Iterable<? extends Cohort> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }

  public PagedModel<EntityModel<Cohort>> toPagedModel(Page<Cohort> cohortPage) {
    Link link = linkTo(CohortController.class).withSelfRel();
    return cohortPageAssembler.toModel(cohortPage, this, link);
  }
}
