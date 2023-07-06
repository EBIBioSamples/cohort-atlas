package uk.ac.ebi.biosamples.cohortatlas.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum DataType {
    @JsonProperty("biospecimens")
    BIOSPECIMENS("biospecimens"),

    @JsonProperty("environmentalData")
    ENVIRONMENTAL_DATA("environmentalData"),
    @JsonProperty("genomicData")
    GENOMIC_DATA("genomicData"),
    @JsonProperty("phenotypicData")
    PHENOTYPIC_DATA("phenotypicData");
    final String value;
    DataType(String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
