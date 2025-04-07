package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetSummary;
import uk.ac.ebi.biosamples.cohortatlas.cohort.CohortSearchRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FacetService {

  private final CohortSearchRepository cohortSearchRepository;

  public FacetService(CohortSearchRepository cohortSearchRepository) {
    this.cohortSearchRepository = cohortSearchRepository;
  }

  public List<Facet> getFacets(String text, List<String> filters) {
    return cohortSearchRepository.getFacets(Objects.requireNonNullElse(text, ""), Objects.requireNonNullElse(filters, new ArrayList<>()));
  }

  public FacetSummary getHighLevelSummary() {
    return getTempHardcodedHighLevelSummary();
  }

  private FacetSummary getTempHardcodedHighLevelSummary() {
    return new FacetSummary(2, 10, 30, 0, 0);
  }
}
