package uk.ac.ebi.biosamples.cohortatlas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacetValue {
  @JsonIgnore
  private String _id;
  private int count;

  private String label;

  public String getLabel() {
    return _id;
  }
}
