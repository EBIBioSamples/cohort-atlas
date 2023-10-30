package uk.ac.ebi.biosamples.cohortatlas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Constant {
    @Id
    private String id;
    private String value;
    private String type;
}
