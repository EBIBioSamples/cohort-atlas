package uk.ac.ebi.biosamples.cohortatlas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CohortController {

  @GetMapping("/test")
  private String test() {
    return "hello";
  }

  @GetMapping("/client")
  public String routeToIndexOfClient() {
    return "../static/index";
  }
}
