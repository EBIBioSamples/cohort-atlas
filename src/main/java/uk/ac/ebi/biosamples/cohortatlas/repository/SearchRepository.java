package uk.ac.ebi.biosamples.cohortatlas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;

import java.util.List;

public interface SearchRepository<T> {

  Page<T> findByFilters(Pageable page, String text, String sort, List<String> filters);


}
