package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Project {
  @Id
  private String accession;
  private String name;
  private String description;
  private String acronym;
  private String website;
  private Provider provider;
  private String rights;
  private String startDate;
  private String endDate;
  private String funding;
  private String acknowledgements;

  private List<Field> dictionary;
}
