package uk.ac.ebi.biosamples.cohortatlas.project;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Map;

public interface ProjectRepository extends MongoRepository<Project, String> {
  @Aggregation(pipeline = {
      "{$group : {_id:\"$cohort\", count:{$sum:1}}}",
      "{$project: {_id: 0, cohort: \"$_id\", fields: \"$count\"}}"})
  AggregationResults<Map<String, Integer>> getFieldCountGroupByCohort();
}
