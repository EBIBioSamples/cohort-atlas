package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public abstract class SearchRepository {
    protected final MongoTemplate mongoTemplate;

    private static final Set<String> booleanFields = new HashSet<>(Arrays.asList("dataTypes"));
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

        for (String filtersStr : filtersList) {
            String[] filterArray = filtersStr.split(":");
            if (filterArray.length > 1) {
                String filterField = filterArray[0];
                String[] filterByValues = filterArray[1].split("~");
                if (booleanFields.contains(filterField)) {
                    for (String filterValue : filterByValues) {
                        query.addCriteria(new Criteria().andOperator(
                            Criteria.where(filterField + "." + filterValue).exists(true),
                            Criteria.where(filterField + "." + filterValue).ne(false))
                        );
                    }
                } else {
                    if (filterByValues.length > 1) {
                        query.addCriteria(Criteria.where(filterField).in(Arrays.asList(filterByValues)));
                    } else {
                        query.addCriteria(Criteria.where(filterField).is(filterByValues[0]));
                    }
                }
            }
        }

    }

    public List<Facet> getFacets() {

     /* using mongoOperations
     TypedAggregation<Cohort> aggregation = Aggregation.newAggregation(Cohort.class,
                  // Add your aggregation stages here to facet the data
                  // Example: Group by genre and count the books in each genre
                  Aggregation.group("license").count().as("count"),
                  Aggregation.project("count").and("license").previousOperation(),
                  Aggregation.group("treatment").count().as("count"),
                  Aggregation.project("count").and("treatment").previousOperation()

                  );
          return mongoOperations.aggregate(aggregation, "cohort", FacetResult.class);*/


        FacetOperation facetOperation = Aggregation.facet()
            .and(Aggregation.unwind("dataSummary.treatment"),
                Aggregation.sortByCount("dataSummary.treatment"),
                Aggregation.match(Criteria.where("_id").nin(null, ""))).as("treatment")
            .and(Aggregation.sortByCount("license"),
                Aggregation.match(Criteria.where("_id").nin(null, ""))).as("license")
            .and(Aggregation.unwind("territories"),
                Aggregation.sortByCount("territories"),
                Aggregation.match(Criteria.where("_id").nin(null, "")))
            .as("territories")
            .and(Aggregation.project().and(ObjectOperators.ObjectToArray.valueOfToArray("dataTypes")).as("dataKeys"),
                Aggregation.unwind("dataKeys"),
                Aggregation.match(Criteria.where("dataKeys.v").is(true)),
                Aggregation.sortByCount("dataKeys.k")).as("dataType");


        AggregationResults<FacetResult> results = mongoTemplate.aggregate(Aggregation.newAggregation(facetOperation), "cohort", FacetResult.class);

        return convertToFacet(results.getMappedResults());
    }

    private List<Facet> convertToFacet(List<FacetResult> mappedResults) {
        List<Facet> facets = new ArrayList<>();

        if(mappedResults == null || mappedResults.size() ==0) {
            return facets;
        }

        return mappedResults.get(0).getFacets();
    }

}
