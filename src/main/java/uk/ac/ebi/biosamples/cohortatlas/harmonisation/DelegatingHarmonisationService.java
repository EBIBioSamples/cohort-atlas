package uk.ac.ebi.biosamples.cohortatlas.harmonisation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.util.List;
import java.util.Map;

@Service
public class DelegatingHarmonisationService implements HarmonisationService {
  private final WebClient webClient;
  @Value("${cohort-atlas.harmonisation-api:localhost:5000}")
  private String pythonModuleUrl;

  public DelegatingHarmonisationService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl(pythonModuleUrl).build();
  }

  @Override
  public List<Field> harmoniseDictionary(String accession, List<Field> dictionary) {
    return delegateToPythonModule(accession, dictionary);
  }

  private List<Field> delegateToPythonModule(String accession, List<Field> dictionary) {
    Map<String, Object> request = Map.of("name", accession, "dictionary", dictionary);
    return this.webClient.post().uri("/harmonise").bodyValue(request)
        .retrieve().bodyToMono(new ParameterizedTypeReference<List<Field>>() {
        }).block();
  }
}
