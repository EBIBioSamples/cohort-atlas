package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Suggestion {
  private String fieldName;
  private float matchPercentage;
}
