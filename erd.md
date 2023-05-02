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
        ontology ontology
    }

    CohortField {
        string category
        string name
        type type
        range range
        MappingStrategy mappingStrategy
    }



    CohortField 1--1 StandardField : ""
    Cohort 1--1+ CohortField :"" 
    Cohort 1--1+ Dataset : ""
```