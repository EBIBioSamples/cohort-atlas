package uk.ac.ebi.biosamples.cohortatlas.harmonisation;

import uk.ac.ebi.biosamples.cohortatlas.model.Field;

import java.util.List;

public interface HarmonisationService {
  List<Field> harmoniseDictionary(String accession, List<Field> dictionary);
}
