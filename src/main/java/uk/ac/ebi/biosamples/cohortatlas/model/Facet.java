package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Facet {

  private String category;
  private String displayName;
  private String searchPath;
  private int count;
  private List<FacetValue> values;
}
