package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class SearchQueryHelper {
    public SearchQueryHelper() {
    }

    public Query getSearchQuery(Pageable page, String text, String sort, List<String> filters) {
        Query query = new Query().with(page);
        populateQueryWithSort(query, sort);
        populateQueryWithFreeTextSearch(query, text);
        populateQueryWithFilters(query, filters);
        return query;
    }

    void populateQueryWithSort(Query query, String sort) {
        if (StringUtils.hasText(sort)) {
            query.with(Sort.by(sort).ascending());
        }
    }

    void populateQueryWithFreeTextSearch(Query query, String text) {
        if (StringUtils.hasText(text)) {
            query.addCriteria(TextCriteria.forDefaultLanguage().matchingAny(text));
        }
    }

    void populateQueryWithFilters(Query query, List<String> filters) {
        if (CollectionUtils.isEmpty(filters)) {
            return;
        }

        for (String filter : filters) {
            String[] filterParts = filter.split(":");
            if (filterParts.length > 1) {
                String field = filterParts[0];
                String value = filterParts[1];
                query.addCriteria(Criteria.where(field).is(value));
            } else {
                throw new IllegalArgumentException("filter should contain a colon");
            }
        }
    }
}