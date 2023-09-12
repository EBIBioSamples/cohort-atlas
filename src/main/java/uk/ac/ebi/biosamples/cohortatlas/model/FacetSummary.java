package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacetSummary {
  private long projects;
  private long datasets;
  private long variables;
  private long harmonisedVariables;
  private long links;
}
