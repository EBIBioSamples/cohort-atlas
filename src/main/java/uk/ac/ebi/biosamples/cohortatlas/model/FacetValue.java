package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacetValue {
  private String label;
  private int count;
}
