package uk.ac.ebi.biosamples.cohortatlas.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"name", "label", "description", "type", "values", "parent", "suggestions", "annotation", "tags"})
public class Field {
  private String name;
  private String label;
  private String description;
  private String type;
  private String values;

  private String parent;
  private List<String> suggestions;
//  private String suggestions;
  private String annotation;
//  private List<String> tags;
  private String tags;
}
