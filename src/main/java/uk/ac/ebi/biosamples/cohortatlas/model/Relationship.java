package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Relationship {
  @Id
  private String id;
  private String source; //should be in format prefix:accession
  private String target; //should be a cohort id
  private Type type;

//  private String altUrl;
//  private String altLabel;


  public enum Type {
    IS_RELATED_TO,
    IS_A_CHILD_OF,
    IS_A_PART_OF
  }
}
