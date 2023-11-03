package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.ac.ebi.biosamples.cohortatlas.CohortAtlasIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
class FieldControllerIntegrationTest extends CohortAtlasIntegrationTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void fields_api_should_return_paginated_fields() {
    String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/fields", String.class);
    assertThat(response).contains("Hypertension");
  }

  @Test
  public void fields_api_text_search_should_return_paginated_fields() {
    String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/fields?text=asthma", String.class);
    assertThat(response).contains("Chronic pulmonary disease (not asthma)");
    assertThat(response).doesNotContain("Hypertension");
  }
}