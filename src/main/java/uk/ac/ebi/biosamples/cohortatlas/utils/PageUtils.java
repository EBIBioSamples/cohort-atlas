package uk.ac.ebi.biosamples.cohortatlas.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

  public static Pageable getPageRequest(Integer page, Integer size) {
    if (page == null || page < 0) {
      page = 0;
    }
    if (size == null || size < 1) {
      size = 10;
    }
    return PageRequest.of(page, size);
  }
}
