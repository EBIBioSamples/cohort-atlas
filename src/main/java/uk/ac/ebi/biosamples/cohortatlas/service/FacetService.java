package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetResult;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetSummary;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetValue;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortSearchRepository;

import java.util.List;

@Service
public class FacetService {

    private final CohortSearchRepository cohortSearchRepository;

    public FacetService(CohortSearchRepository cohortSearchRepository) {
        this.cohortSearchRepository = cohortSearchRepository;
    }

    public List<FacetResult> getFacets() {
    return cohortSearchRepository.getFacets();
  }

  public FacetSummary getHighLevelSummary() {
    return getTempHardcodedHighLevelSummary();
  }

    private FacetSummary getTempHardcodedHighLevelSummary() {
    return new FacetSummary(2, 10, 30, 0, 0);
  }
}
