package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class DataTypes {
  private String biospecimens;
  private String environmentalData;
  private String genomicData;
  private String phenotypicData;
}
