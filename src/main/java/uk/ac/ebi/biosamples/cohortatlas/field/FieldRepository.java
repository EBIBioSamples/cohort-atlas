package uk.ac.ebi.biosamples.cohortatlas.field;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;

public interface FieldRepository extends MongoRepository<DictionaryField, String> {
  List<DictionaryField> findByCohort(String cohort);

  @Aggregation(pipeline = {
      "{$group : {_id:\"$cohort\", count:{$sum:1}}}",
      "{$project: {_id: 0, cohort: \"$_id\", fields: \"$count\"}}"})
  AggregationResults<Map<String, Integer>> getFieldCountGroupByCohort();

}
