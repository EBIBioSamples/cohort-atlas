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
import uk.ac.ebi.biosamples.cohortatlas.field.FieldController;
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class RelationshipModelAssembler implements RepresentationModelAssembler<Relationship, EntityModel<Relationship>> {

  private final PagedResourcesAssembler<Relationship> fieldPageAssembler;

  public RelationshipModelAssembler(PagedResourcesAssembler<Relationship> fieldPageAssembler) {
    this.fieldPageAssembler = fieldPageAssembler;
  }

  @Override
  public EntityModel<Relationship> toModel(@NonNull Relationship relationship) {
    Link link = linkTo(FieldController.class).slash(relationship.getId()).withSelfRel();
    return EntityModel.of(relationship, link);
  }

  @Override
  public CollectionModel<EntityModel<Relationship>> toCollectionModel(Iterable<? extends Relationship> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }

  public PagedModel<EntityModel<Relationship>> toPagedModel(Page<Relationship> fieldPage) {
    Link link = linkTo(FieldController.class).withSelfRel();
    return fieldPageAssembler.toModel(fieldPage, this, link);
  }
}
