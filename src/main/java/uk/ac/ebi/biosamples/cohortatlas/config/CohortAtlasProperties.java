package uk.ac.ebi.biosamples.cohortatlas.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CohortAtlasProperties {
  @Value("${cohort-atlas.auth.webin-api:https://www.ebi.ac.uk/ena/submit/webin/auth}")
  private String webinUrl;
  @Value("${cohort-atlas.standard-dictionary:mapping/standard_terms.csv}")
  private String standardDictionaryPath;
}
