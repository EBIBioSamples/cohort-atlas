package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Provider {
  private String name;
  private String acronym;
  private String website;
  private String description;

  private String contacts;
  private String logo;
  private String resources;
}
