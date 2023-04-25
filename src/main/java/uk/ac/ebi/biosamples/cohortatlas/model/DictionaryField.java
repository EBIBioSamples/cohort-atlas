package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class DictionaryField {
  @Id
  @TextIndexed(weight = 10F)
  private String id; //?
  @TextIndexed(weight = 10F)
  private String name;
  @TextIndexed(weight = 10F)
  private String label;
  @TextIndexed(weight = 10F)
  private String description;
  private String type;
  private String values;

  private String parent;
  private List<String> annotations;
  private List<String> tags;

  private String fullQualifiedName; // get name with parent paths?

  private String cohort;
}
