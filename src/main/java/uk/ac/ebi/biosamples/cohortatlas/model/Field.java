package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
//simplification of dictionary field todo remove after MVP?
public class Field {
  private String name;
  private String description;
  private String mappedTerm;
}
