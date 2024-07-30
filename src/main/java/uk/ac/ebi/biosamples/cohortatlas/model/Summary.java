package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

import java.util.List;

@Data
public class Summary {
  private List<String> diseases;
  private List<String> medication;
  private List<String> treatment;
  private List<String> outcome;
  private List<String> complications;
  private List<String> typeOfData;

  private String ageRange;
  private List<String> ageGroups;
  private long sampleSize;
  private String sampleType;
  private String followUpSchedule;

  private String collectionType;
  private String inclusionCriteria;
  private String releaseType;
  private String linkageOptions;
  private List<Double> taxId;
  private List<String> scientificName;

}
