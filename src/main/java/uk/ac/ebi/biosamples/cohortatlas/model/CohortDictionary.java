package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class CohortDictionary {
  private String field;
  private String description;
  private String values;
  private String type;
  private String tags;
}
