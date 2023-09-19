package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FacetResult {


    private List<FacetValue> treatment;
    private List<FacetValue> license;
    private List<FacetValue> territories;

}
