package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FieldControllerIntegrationTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void fields_api_should_return_paginated_fields() {
    String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/fields", String.class);
    assertThat(response).contains("alcohol_use_history");
    assertThat(response).contains("tobacco_use_history");
  }

  @Test
  public void fields_api_text_search_should_return_paginated_fields() {
    String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/fields?text=alcohol", String.class);
    assertThat(response).contains("alcohol_use_history");
    assertThat(response).doesNotContain("tobacco_use_history");
  }
}