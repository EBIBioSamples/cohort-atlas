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
import uk.ac.ebi.biosamples.cohortatlas.controller.FieldController;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.model.DictionaryField;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class FieldModelAssembler implements RepresentationModelAssembler<DictionaryField, EntityModel<DictionaryField>> {

  private final PagedResourcesAssembler<DictionaryField> fieldPageAssembler;

  public FieldModelAssembler(PagedResourcesAssembler<DictionaryField> fieldPageAssembler) {
    this.fieldPageAssembler = fieldPageAssembler;
  }

  @Override
  public EntityModel<DictionaryField> toModel(@NonNull DictionaryField field) {
    Link link = linkTo(FieldController.class).slash(field.getId()).withSelfRel();
    return EntityModel.of(field, link);
  }

  @Override
  public CollectionModel<EntityModel<DictionaryField>> toCollectionModel(Iterable<? extends DictionaryField> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }

  public PagedModel<EntityModel<DictionaryField>> toPagedModel(Page<DictionaryField> fieldPage) {
    Link link = linkTo(FieldController.class).withSelfRel();
    return fieldPageAssembler.toModel(fieldPage, this, link);
  }
}
