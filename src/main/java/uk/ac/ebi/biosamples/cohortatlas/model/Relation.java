package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Relation {
  private String source; //should be in format prefix:accession
  private Type type;

  public enum Type {
    IS_RELATED_TO,
    IS_A_CHILD_OF,
    IS_A_PART_OF
  }
}
