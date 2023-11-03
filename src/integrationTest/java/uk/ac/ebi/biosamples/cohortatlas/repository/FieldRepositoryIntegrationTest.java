package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.ebi.biosamples.cohortatlas.KeycloakTestContainers;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class FieldRepositoryIntegrationTest extends KeycloakTestContainers {
  @Autowired
  public FieldRepository fieldRepository;

  @Test
  public void fieldGroupByCount_should_return_cohortFieldCount() {
    AggregationResults<Map<String, Integer>> result = fieldRepository.getFieldCountGroupByCohort();
    List<Map<String, Integer>> countList = result.getMappedResults();
    Assertions.assertTrue(countList.size() > 1);
  }
}
