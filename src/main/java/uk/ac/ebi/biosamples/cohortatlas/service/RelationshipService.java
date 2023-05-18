package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Relationship;
import uk.ac.ebi.biosamples.cohortatlas.repository.RelationshipRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.RelationshipSearchRepository;

import java.util.List;

@Service
public class RelationshipService {
  private final RelationshipRepository relationshipRepository;

  public RelationshipService(RelationshipRepository relationshipRepository, RelationshipSearchRepository relationshipSearchRepository) {
    this.relationshipRepository = relationshipRepository;
  }

  public Page<Relationship> searchRelationships(Pageable pageRequest, String text, List<String> filters, String sort) {
    return relationshipRepository.findByFilters(pageRequest, text, sort, filters);
  }

}
