package uk.ac.ebi.biosamples.cohortatlas.model.export;

import java.util.List;

public class EbiSearchCohort {
  private String id;

  private int cohortId;
  private String pid;

  private String studyName;
  private String name;

  private String acronym;

  private String studyAim;
  private String description;

  private String website;

  private String organisation;
  private String institution;

  private Object recruitmentDate;
  private String startYear;
  private String endYear;

  private int confirmedCount;
  private int numberOfParticipants;

  private String countries;


  private Object ageRange;
  private List<Object> populationAgeGroups;

  private String population;
  private String inclusionCriteria;


  private int sampleSize;
  private String sampleType;
  private String followUpSchedule;
  private String location;
  private List<Object> comorbidities;
  private List<Object> medication;
  private List<Object> typeOfData;
  private List<Object> treatment;
  private List<Object> outcome;


  private String type;
  private String design;
  private String[] collectionType;
  private String[] regions;
  private String dataAccessConditions;
  private String dataAccessConditionsDescription;
  private String releaseType;
  private String linkageOptions;
  private String designPaper;
  private String[] publications;
  private String fundingStatement;
  private String acknowledgements;
}
