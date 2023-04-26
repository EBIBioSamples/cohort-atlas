```mermaid
erDiagram
    Cohort {
    string name
    number enrollment
    }

    Dataset {
        string name
        URL url
    }

    StandardField {
        string name
        type type
    }

    CohortDescriptor {
        CohortField[] fields
    }

    Cohort 1--1 CohortDescriptor: ""
    CohortField {
        string name
        type type
        range range
    }

    CohortDescriptor 1--1+CohortField:""
    FieldHarmonisationMapping {
        CohortField cohortField
        StandardField standardField
        MappingStrategy mappingStrategy
    }

    FieldHarmonisationMapping 1+--1+ CohortField : ""
    FieldHarmonisationMapping 1+--1+ StandardField : ""

    Cohort 1--1+ Dataset : d
```