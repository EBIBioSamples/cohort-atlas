package uk.ac.ebi.biosamples.cohortatlas.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CohortAtlasProperties {

    @Value("${auth.api.url:https://www.ebi.ac.uk/ena/submit/webin/auth}")
    private String webinUrl;
}
