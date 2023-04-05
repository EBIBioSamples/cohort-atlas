package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Relationship {
  private String prefix;
  private String accession;
  private Type type;

  private String cohort;


//  private String altUrl;
//  private String altLabel;
//  private String type;


  public enum Type {
    IS_RELATED_TO,
    IS_A_CHILD_OF
  }
}
