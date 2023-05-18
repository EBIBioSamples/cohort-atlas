package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
//simplification of relationship todo remove after MVP?
public class Link {
  private Archive archive;
  private String source; // accession
  private Type type;


  public enum Type {
    IS_RELATED_TO,
    IS_A_CHILD_OF,
    IS_A_PART_OF
  }

  public enum Archive {
    BIOSAMPLES("biosample");

    private final String prefix;
    Archive(String identifiersDotOrgPrefix) {
      this.prefix = identifiersDotOrgPrefix;
    }

    public String toIdentifiersDotOrgPrefix() {
      return prefix;
    }
  }
}
