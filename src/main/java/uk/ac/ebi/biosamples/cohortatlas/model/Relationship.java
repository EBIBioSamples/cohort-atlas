package uk.ac.ebi.biosamples.cohortatlas.model;

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
