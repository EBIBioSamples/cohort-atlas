package uk.ac.ebi.biosamples.cohortatlas.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class NoSplitStringConverter implements Converter<String, List<String>> {
  @Override
  public List<String> convert(String source) {
    return Collections.singletonList(source);
  }
}
