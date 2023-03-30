package uk.ac.ebi.biosamples.cohortatlas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document
public class Cohort extends CohortDescriptor {
  @Id
  private String accession;
  private String name;

  private String cohortId;
  private String cohortName;
  private String description;
  private String acronym;
  private String website;
  private String logo;
  private String type;
  private Provider provider;
  private String license;
  private String rights;
  private Ontology dataSharing;
  private List<Contact> contacts;
  private List<Contact> investigators;
  private Instant startDate;
  private Instant endDate;
  private long targetEnrollment;
  private long totalEnrollment;
  private String publications;
  private String funding;
  private String acknowledgements;
  private String supplementaryInformation;
  private String datasets;
  private String territories;
  private DataTypes dataTypes;
  private String projects;
  private List<Attachment> attachments;

  private List<String> dictionary;
  private List<String> links;
}
