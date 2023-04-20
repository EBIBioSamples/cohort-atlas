package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class DataTypes {
  private boolean biospecimens;
  private boolean environmentalData;
  private boolean genomicData;
  private boolean phenotypicData;
}
