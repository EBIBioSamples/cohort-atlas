package uk.ac.ebi.biosamples.cohortatlas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CohortAtlasApplicationTest extends CohortAtlasIntegrationTest {

  @Test
  void contextLoads() {
  }

}
