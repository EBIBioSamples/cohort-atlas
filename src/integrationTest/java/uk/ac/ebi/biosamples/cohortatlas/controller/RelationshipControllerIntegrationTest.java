package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.ac.ebi.biosamples.cohortatlas.CohortAtlasIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
class RelationshipControllerIntegrationTest extends CohortAtlasIntegrationTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void relationships_api_should_return_paginated_relationships() {
    assertThat(
        this.restTemplate.getForObject("http://localhost:" + port + "/api/relationships", String.class))
        .contains("test_relationship_2");
  }
}