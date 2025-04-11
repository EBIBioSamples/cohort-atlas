package uk.ac.ebi.biosamples.cohortatlas.harmonisation.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HarmonisingField {
  @JsonProperty("TERM")
  private String term;
  @JsonProperty("DESCRIPTION")
  private String description;
  @JsonProperty("MAPPING")
  private String mapping;
}
