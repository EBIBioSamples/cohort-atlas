package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Attachment {
  private String name;
  private String resource;
  private String hyperlink;
  private String file;
  private String description;
}
