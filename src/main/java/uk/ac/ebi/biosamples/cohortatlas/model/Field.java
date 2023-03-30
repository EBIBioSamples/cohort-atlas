package uk.ac.ebi.biosamples.cohortatlas.model;

import java.util.List;

public class Field {
  private String id; //?
  private String name;
  private String label;
  private String description;
  private String type;
  private String values;

  private String parent;
  private List<String> annotations;
  private List<String> tags;

  private String cohort;
}
