package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryField {
  private String id; //?
  private String name;
  private String label;
  private String description;
  private String type;
  private String values;

  private String parent;
  private List<String> annotations;
  private List<String> tags;

  private String fullQualifiedName; // get name with parent paths?

  private String cohort;
}
