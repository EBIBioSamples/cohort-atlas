package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.util.List;

@Service
public class HarmonisationService {
  private final WebClient webClient;

  public HarmonisationService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
  }

  public Mono<Field> harmoniseDictionary(List<Field> dictionary) {
    return this.webClient.post().uri("/harmonise").bodyValue(dictionary)
        .retrieve().bodyToMono(Field.class);
  }

}
