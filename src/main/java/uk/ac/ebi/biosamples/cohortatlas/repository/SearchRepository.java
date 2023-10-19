package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetResult;

import java.util.*;

@Repository
public abstract class SearchRepository {
  private static final Set<String> booleanFields = new HashSet<>(Arrays.asList("dataTypes"));
  protected final MongoTemplate mongoTemplate;

  public SearchRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  protected Query getSearchQuery(Pageable page, String text, String sort, List<String> filters) {
    Query query = new Query().with(page);
    populateQueryWithSort(query, sort);
    populateQueryWithFreeTextSearch(query, text);
    populateQueryWithFilters(query, filters);
    return query;
  }

  private void populateQueryWithSort(Query query, String sort) {
    if (StringUtils.hasText(sort)) {
      query.with(Sort.by(sort).ascending());
    }
  }

  private void populateQueryWithFreeTextSearch(Query query, String text) {
    if (StringUtils.hasText(text)) {
      query.addCriteria(TextCriteria.forDefaultLanguage().matchingAny(text));
    }
  }

  private void populateQueryWithFilters(Query query, List<String> filtersList) {
    if (CollectionUtils.isEmpty(filtersList)) {
      return;
    }

    List<Criteria> criteriaList = filtersList.stream().map(f -> {
      String[] parts = f.split(":");
      return Criteria.where(parts[0]).is(parts[1]);
    }).toList();

    query.addCriteria(new Criteria().andOperator(criteriaList));

//        for (String filtersStr : filtersList) {
//            String[] filterArray = filtersStr.split(":");
//            if (filterArray.length > 1) {
//                String filterField = filterArray[0];
//                String[] filterByValues = filterArray[1].split("~");
//                if (booleanFields.contains(filterField)) {
//                    for (String filterValue : filterByValues) {
//                        query.addCriteria(new Criteria().andOperator(
//                            Criteria.where(filterField + "." + filterValue).exists(true),
//                            Criteria.where(filterField + "." + filterValue).ne(false))
//                        );
//                    }
//                } else {
//                    if (filterByValues.length > 1) {
//                        query.addCriteria(Criteria.where(filterField).in(Arrays.asList(filterByValues)));
//                    } else {
//                        query.addCriteria(Criteria.where(filterField).is(filterByValues[0]));
//                    }
//                }
//            }
//        }

  }

  public List<Facet> getFacets(String text, List<String> filters) {
    List<AggregationOperation> operations = new ArrayList<>();

    if (StringUtils.hasText(text)) {
      TextCriteria searchCriteria = TextCriteria.forDefaultLanguage().matchingAny(text);
      operations.add(Aggregation.match(searchCriteria));
    }

    if (!CollectionUtils.isEmpty(filters)) {
      List<Criteria> criteriaList = filters.stream().map(f -> {
        String[] parts = f.split(":");
        return Criteria.where(parts[0]).is(parts[1]);
      }).toList();

      Criteria filterCriteria = new Criteria().andOperator(criteriaList);
      operations.add(Aggregation.match(filterCriteria));
    }

    FacetOperation facetOperation = Aggregation.facet()
        .and(Aggregation.unwind("dataSummary.treatment"),
            Aggregation.sortByCount("dataSummary.treatment"),
            Aggregation.match(Criteria.where("_id").nin(null, ""))).as("treatment")
        .and(Aggregation.unwind("dataSummary.diseases"),
            Aggregation.sortByCount("dataSummary.diseases"),
            Aggregation.match(Criteria.where("_id").nin(null, ""))).as("diseases")
        .and(Aggregation.unwind("dataSummary.medication"),
            Aggregation.sortByCount("dataSummary.medication"),
            Aggregation.match(Criteria.where("_id").nin(null, ""))).as("medication")
        .and(Aggregation.sortByCount("license"),
            Aggregation.match(Criteria.where("_id").nin(null, ""))).as("license")
        .and(Aggregation.unwind("territories"),
            Aggregation.sortByCount("territories"),
            Aggregation.match(Criteria.where("_id").nin(null, "")))
        .as("territories")
        .and(Aggregation.unwind("dataTypes"),
            Aggregation.sortByCount("dataTypes"),
            Aggregation.match(Criteria.where("_id").nin(null, "")))
        .as("dataTypes");
    operations.add(facetOperation);

    AggregationResults<FacetResult> results =
        mongoTemplate.aggregate(Aggregation.newAggregation(operations), "cohort", FacetResult.class);

    return convertToFacet(results.getMappedResults());
  }

  private List<Facet> convertToFacet(List<FacetResult> mappedResults) {
    List<Facet> facets = new ArrayList<>();
    if (mappedResults == null || mappedResults.isEmpty()) {
      return facets;
    }
    return mappedResults.get(0).getFacets();
  }

}
