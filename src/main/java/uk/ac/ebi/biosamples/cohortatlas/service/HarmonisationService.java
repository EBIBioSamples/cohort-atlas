package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HarmonisationService {
  private final WebClient webClient;

  public HarmonisationService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
  }

  public List<Field> harmoniseDictionary(String accession, List<Field> dictionary) {

    Map<String, Object> request = new HashMap<>();
    request.put("name", accession);
    request.put("dictionary", dictionary);

    return this.webClient.post().uri("/harmonise").bodyValue(request)
        .retrieve().bodyToMono(new ParameterizedTypeReference<List<Field>>() {}).block();
  }

}
