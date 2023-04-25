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
  private final RelationshipSearchRepository relationshipSearchRepository;

  public RelationshipService(RelationshipRepository relationshipRepository, RelationshipSearchRepository relationshipSearchRepository) {
    this.relationshipRepository = relationshipRepository;
    this.relationshipSearchRepository = relationshipSearchRepository;
  }

  public Page<Relationship> searchRelationships(Pageable pageRequest, String text, List<String> filters, String sort) {
    return relationshipSearchRepository.findFieldPageWithFilters(pageRequest, text, sort, filters);
  }

}
