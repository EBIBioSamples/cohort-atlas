package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

@Data
public class Contact {
  private String name;
  private String email;
  private String orcid;
  private String affiliation;
  private String address;
  private String role;
}
