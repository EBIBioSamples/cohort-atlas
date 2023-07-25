package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;

@Data
public class Field {
  @TextIndexed(weight = 10F)
  private String name;
  @TextIndexed(weight = 10F)
  private String label;
  @TextIndexed(weight = 10F)
  private String description;
  private String type;
  private String values;

  private String parent;
  private String annotation;
  private List<String> tags;
}
