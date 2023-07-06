package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document
@Data
public class Cohort {
  @Transient
  public static final String SEQUENCE_NAME = "accession_sequence";
  @Id
  @TextIndexed(weight = 10F)
  private String accession;
  @TextIndexed(weight = 10F)
  private String cohortName; // study name
  @TextIndexed(weight = 10F)
  private String description; // study aim
  private String acronym;
  private String website;
  private String logo;
  private String type; //Type of the cohort e.g. 'cohort', 'network': could be an ontology?
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
  private List<Publication> publications;
  private String funding;
  private String acknowledgements;
  private String supplementaryInformation;
  private List<Dataset> datasets;
  private List<Ontology> territories; //countries/regions, should we rename this to populations?
  private List<DataType> dataTypes;
  private List<Project> projects;
  private List<Attachment> attachments;

  private List<ExternalLink> externalLinks;

//  private List<String> dataSources;//Method of data collection: Questionnaire, Physical measure, Cognitive measure, Biosample, Government databases/registries, Other source of information
//  private List<String> targets;//Persons the question is about: Participant, Children, Parents, Grandparents, Siblings, Partner, Interviewer, Proxy, Other target
//  private List<String> sampleType; // urine, blood, stool, saliva, other
//  biosample attributes: sample size, processing method, storage methods
//  demographic data: sexes, gender, age range

}
