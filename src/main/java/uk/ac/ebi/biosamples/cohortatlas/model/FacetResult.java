package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FacetResult {

    private List<FacetValue> treatment;
    private List<FacetValue> diseases;
    private List<FacetValue> medication;
    private List<FacetValue> license;
    private List<FacetValue> territories;

    private List<FacetValue> dataTypes;

    public List<Facet> getFacets() {
        return List.of(
            new Facet("dataTypes", "Data Types", "dataTypes", getTotalCount(dataTypes), dataTypes),
            new Facet("treatments", "Treatments", "dataSummary.treatment", getTotalCount(treatment), treatment),
            new Facet("diseases", "Diseases", "dataSummary.diseases", getTotalCount(diseases), diseases),
            new Facet("medication", "Medication", "dataSummary.medication", getTotalCount(medication), medication),
            new Facet("territories", "Territories", "territories", getTotalCount(territories), territories),
            new Facet("license", "License", "license", getTotalCount(license), license));
    }

    private Integer getTotalCount(List<FacetValue> facetValues) {
        int count = 0;
        if (facetValues == null) {
            return count;
        }
        for( FacetValue fv: facetValues) {
          count += fv.getCount();
        }
        return count;
    }
}
