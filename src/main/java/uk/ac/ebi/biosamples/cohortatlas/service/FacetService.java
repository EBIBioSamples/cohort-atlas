package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Facet;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetSummary;
import uk.ac.ebi.biosamples.cohortatlas.model.FacetValue;

import java.util.List;

@Service
public class FacetService {

  public List<Facet> getFacets(String text, List<String> filters) {
    return getTempHardcodedFacets();
  }

  public FacetSummary getHighLevelSummary() {
    return getTempHardcodedHighLevelSummary();
  }

  private List<Facet> getTempHardcodedFacets() {
    return List.of(
        new Facet("dataTypes", "Data Types", "dataTypes", 10, List.of(
            new FacetValue("Biospecimens", 5),
            new FacetValue("Environmental Data", 5),
            new FacetValue("Genomic Data", 5),
            new FacetValue("Phenotypic Data", 5),
            new FacetValue("Other", 5)
        )),
        new Facet("treatments", "Treatments", "dataSummary.treatment", 5, List.of(
            new FacetValue("Antibiotic", 5),
            new FacetValue("Any Oxygen therapy", 5),
            new FacetValue("Chloroquine/hydroxychloroquine", 5)
        )),
        new Facet("diseases", "Diseases", "dataSummary.diseases", 5, List.of(
            new FacetValue("Hypertension", 5),
            new FacetValue("Obesity (as defined by clinical staff)", 5),
            new FacetValue("Chronic hematologic disease", 5),
            new FacetValue("Tuberculosis", 5)
        )),
        new Facet("medications", "Medications", "dataSummary.medication", 5, List.of(
            new FacetValue("Antibiotic", 5),
            new FacetValue("Non-steroidal anti-inflammatory (NSAID)", 5),
            new FacetValue("Antivirals", 5),
            new FacetValue("Oral steroids", 5)
        )),
        new Facet("outcomes", "Outcomes", "dataSummary.outcome", 5, List.of(
            new FacetValue("Discharged alive", 5),
            new FacetValue("Palliative discharge", 5),
            new FacetValue("Transfer to other facility", 5),
            new FacetValue("Death", 5)
        )),
        new Facet("complications", "Complications", "dataSummary.complications", 5, List.of(
            new FacetValue("Bronchiolitis", 5),
            new FacetValue("Cardiac arrest", 5),
            new FacetValue("Myocardial infarction", 5),
            new FacetValue("Pneumothorax", 5),
            new FacetValue("Bacterial pneumonia", 5)
        ))
    );
  }

  private FacetSummary getTempHardcodedHighLevelSummary() {
    return new FacetSummary(2, 10, 30, 0, 0);
  }
}
