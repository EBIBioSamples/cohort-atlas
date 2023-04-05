package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class ExternalLink {
  private String label;
  private String url;
  private String type;
  private String archive;

  private String cohort;
}
