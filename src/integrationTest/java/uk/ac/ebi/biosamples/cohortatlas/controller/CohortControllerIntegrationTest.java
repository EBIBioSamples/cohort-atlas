package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ebi.biosamples.cohortatlas.CohortAtlasIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CohortControllerIntegrationTest extends CohortAtlasIntegrationTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getCohorts() {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/cohorts", String.class)).contains("ReCoDID");
  }

  @Test
  void getCohortsWithMockMvc() throws Exception  {
    this.mockMvc.perform(get("/api/cohorts")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("ReCoDID")));
  }

  @Test
  public void free_text_search() {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/cohorts?text=recodid", String.class)).contains("ReCoDID");
  }
}