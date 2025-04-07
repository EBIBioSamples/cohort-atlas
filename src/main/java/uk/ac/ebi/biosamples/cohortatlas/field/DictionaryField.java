package uk.ac.ebi.biosamples.cohortatlas.field;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

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
  private String annotation;
  private List<String> tags;

  private String fullQualifiedName; // get name with parent paths?

  private String cohort;
  private String project;

  public static DictionaryField from(Field field) {
    DictionaryField dictionaryField = new DictionaryField();
    dictionaryField.setName(field.getName());
    dictionaryField.setLabel(field.getLabel());
    dictionaryField.setDescription(field.getDescription());
    dictionaryField.setType(field.getType());
    dictionaryField.setValues(field.getValues());
    dictionaryField.setParent(field.getParent());
    dictionaryField.setAnnotation(field.getAnnotation());
    return dictionaryField;
  }
}
