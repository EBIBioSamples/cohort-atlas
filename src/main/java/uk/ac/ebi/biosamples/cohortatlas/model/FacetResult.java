package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import static uk.ac.ebi.biosamples.cohortatlas.model.Facet.*;

import java.util.List;

@Data
@AllArgsConstructor
public class FacetResult {

    private List<FacetValue> treatments;
    private List<FacetValue> license;
    private List<FacetValue> territories;

    private List<FacetValue> dataTypes;

    public List<Facet> getFacets() {
        return List.of(new Facet(FacetType.DATA_TYPES.category , FacetType.DATA_TYPES.displayName, FacetType.DATA_TYPES.searchPath,
                        getTotalCount(dataTypes), dataTypes),
                new Facet(FacetType.TREATMENTS.category, FacetType.TREATMENTS.displayName, FacetType.TREATMENTS.searchPath,
                        getTotalCount(treatments), treatments),
                new Facet(FacetType.TERRITORIES.category, FacetType.TERRITORIES.displayName, FacetType.TERRITORIES.searchPath,
                        getTotalCount(territories), territories),
                new Facet(FacetType.LICENSE.category, FacetType.LICENSE.displayName, FacetType.LICENSE.searchPath,
                        getTotalCount(license), license));
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
