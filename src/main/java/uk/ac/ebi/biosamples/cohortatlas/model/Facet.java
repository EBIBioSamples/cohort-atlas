package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Facet {

  private String category;
  private String displayName;
  private String searchPath;
  private int count;
  private List<FacetValue> values;

   public enum FacetType {
    DATA_TYPES("dataTypes", "Data Types", "dataTypes"),
    TREATMENTS("treatments", "Treatments", "dataSummary.treatment"),
    LICENSE("license", "License", "license"),
    TERRITORIES("territories", "Territories", "territories");
    FacetType(String category, String displayName, String searchPath){
      this.category = category;
      this.displayName = displayName;
      this.searchPath = searchPath;
    }
    public final String category;
    public final String displayName;
    public final String searchPath;
  }
}
