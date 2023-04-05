package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Provider {
  private String acronym;
  private String name;
  private String logo;
  private String description;
  private String website;
  private String contacts;
  private String resources;
}
