package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;

import java.util.List;
@Data
public class Facet {
     List<String> dataTypes;
     List<String> licences;
     List<String> treatments;
}
