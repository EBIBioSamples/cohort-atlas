package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.repository.CohortRepository;
import uk.ac.ebi.biosamples.cohortatlas.repository.SearchRepository;

import java.util.List;

@Service
public class CohortService {
  private final CohortRepository cohortRepository;
  private final SearchRepository searchRepository;

  public CohortService(CohortRepository cohortRepository, SearchRepository searchRepository) {
    this.cohortRepository = cohortRepository;
    this.searchRepository = searchRepository;
  }

  public Cohort saveCohort(Cohort cohort) {
    return cohortRepository.save(cohort);
  }

  public Page<Cohort> searchCohorts(Pageable pageRequest, String text, List<String> filters, String sort) {
    return searchRepository.findPageWithFilters(pageRequest, text, sort, filters);
  }

  @NonNull
  public Cohort getCohortById(String id) {
    return cohortRepository.findById(id).orElseThrow(() -> new RuntimeException("Cohort does not exist: " + id));
  }

  public void deleteCohort(String id) {
    cohortRepository.deleteById(id);
  }

  public Pageable getPageRequest(Integer page, Integer size) {
    if (page == null || page < 0) {
      page = 0;
    }
    if (size == null || size < 1) {
      size = 10;
    }
    return PageRequest.of(page, size);
  }
}
