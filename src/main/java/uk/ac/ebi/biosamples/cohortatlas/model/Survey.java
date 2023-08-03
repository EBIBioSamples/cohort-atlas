package uk.ac.ebi.biosamples.cohortatlas.model;

public class Survey {
  private Ethnicity ethnicity;
  private Biosample biosamples;
  private GenomicData genomicData;
  private Exposure exposure;

  public class Ethnicity {
    private String asian;
    private String african;
    private String european;
    private String hispanic;
    private String middleEastern;
    private String other;
  }

  public class Biosample {
    private String plasmaSample;
    private String bloodSample;
    private String salivaSample;
    private String cerebrospinalFluid;
    private String cells;
    private String specificTissue;
    private String urine;
    private String feces;
    private String placenta;
    private String umbilicalCordBlood;
    private String breastMilk;
    private String hairNail;
    private String other;
  }

  public class GenomicData {
    private String genotypeArrays;
    private String wholeExomes;
    private String wholeGenomes;
    private String other;
  }

  public class Exposure {
    private String lifestyle;
    private String builtEnvironment;
    private String weatherAtmosphere;
    private String stress;
    private String toxicOrChemicalExposure;
    private String dietAndMedications;
    private String socialDeterminantsOfHealth;
    private String other;
  }
}
