package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    for(String filterValue: filterByValues) {
                        query.addCriteria(new Criteria().andOperator(
                                Criteria.where(filterField+"."+filterValue).exists(true),
                                Criteria.where(filterField+"."+filterValue).ne(false))
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


}
