import argparse
import json
import re
from copy import deepcopy
from pathlib import Path

import pandas as pd


# Column in the Excel template containing the values to map into JSON
VALUE_COLUMN = "#Value(TODO)"

PERSON_FIELD_MAPPINGS = {
    "name": "name",
    "email": "email",
    "ORCiD": "orcid",
    "address": "address"
}
PERSON_SECTIONS = {"contacts", "investigators"}


# Base JSON structure based on the Cohort Atlas Cohort model.
# The script fills this template using values from the Excel file.
EMPTY_COHORT_TEMPLATE = {
    "accession": "",
    "cohortName": "",
    "description": "",
    "acronym": "",
    "website": "",
    "type": "Cohort",
    "studyDesign": "",
    "provider": {
        "name": "",
        "website": "",
        "description": ""
    },
    "contacts": [],
    "investigators": [],
    "startDate": "",
    "endDate": "",
    "targetEnrollment": 0,
    "totalEnrollment": 0,
    "publications": [],
    "funding": "",
    "territories": [],
    "project": "",
    "dataSummary": {
        "diseases": [],
        "medication": [],
        "treatment": [],
        "outcome": [],
        "complications": [],
        "typeOfData": [],
        "ageRange": "",
        "sampleSize": 0,
        "sampleType": "",
        "followUpSchedule": "",
        "inclusionCriteria": "",
        "taxId": [],
        "hostTaxId": []
    },
    "tags": [],
    "label": "",
    "_class": "uk.ac.ebi.biosamples.cohortatlas.cohort.Cohort"
}


def clean(value):
    """Clean Excel cell values.

    Converts empty/NaN values to None and strips whitespace from strings.
    """
    if pd.isna(value):
        return None

    value = str(value).strip()
    return value if value else None


def split_list(value):
    """Convert comma-separated Excel values into a JSON list."""
    value = clean(value)

    if not value:
        return []

    # Treat common empty indicators as no value
    if value.lower() in ["none", "no", "n/a", "na"]:
        return []

    return [item.strip() for item in value.split(",") if item.strip()]


def to_int(value):
    """Convert Excel numeric values into integers.

    Empty values are converted to 0.
    """
    value = clean(value)

    if not value:
        return 0

    try:
        return int(float(value))
    except ValueError:
        return 0


def normalize_date(value):
    """Normalize date values to YYYY-MM-DD, while allowing YYYY-only dates."""
    value = clean(value)

    if not value:
        return ""

    if re.fullmatch(r"\d{4}", value):
        return value

    date_formats = [
        "%Y-%m-%d",
        "%d-%m-%Y",
        "%d/%m/%Y",
        "%Y/%m/%d",
        "%d.%m.%Y",
        "%d %m %Y",
        "%d %b %Y",
        "%d %B %Y",
        "%b %d %Y",
        "%B %d %Y"
    ]

    for date_format in date_formats:
        parsed_date = pd.to_datetime(value, format=date_format, errors="coerce")
        if not pd.isna(parsed_date):
            return parsed_date.strftime("%Y-%m-%d")

    parsed_date = pd.to_datetime(value, dayfirst=True, errors="coerce")
    if not pd.isna(parsed_date):
        return parsed_date.strftime("%Y-%m-%d")

    return value


def build_lookup(df, value_column):
    """Convert the Excel template into a flat lookup dictionary.

    Example:
      Overview.cohortName -> "International Research Consortium..."
      Study Design.totalEnrollment -> "109"

    This makes the rest of the mapping easier and keeps Excel parsing
    separate from JSON generation.
    """
    lookup = {}
    current_section = None
    person_section_state = {
        section: {"index": 1, "seen_fields": set()}
        for section in PERSON_SECTIONS
    }

    for _, row in df.iterrows():
        field_type = clean(row.get("#FieldType"))
        field_name = clean(row.get("#FieldName"))
        value = clean(row.get(value_column))

        # #FieldType marks the current section, for example "Overview"
        if field_type:
            current_section = field_type

        # Skip rows without useful content
        if not current_section or not field_name or not value:
            continue

        # Skip section header rows, for example Overview / Overview
        if field_name == current_section:
            continue

        if current_section in PERSON_SECTIONS and field_name in PERSON_FIELD_MAPPINGS:
            state = person_section_state[current_section]

            if field_name in state["seen_fields"]:
                state["index"] += 1
                state["seen_fields"] = set()

            lookup_key = f"{current_section}[{state['index']}].{field_name}"
            state["seen_fields"].add(field_name)
        else:
            lookup_key = f"{current_section}.{field_name}"

        lookup[lookup_key] = value

    return lookup


def get(lookup, key, default=""):
    """Small helper to safely retrieve values from the lookup dictionary."""
    return lookup.get(key, default)


def get_type_of_data(lookup):
    """Extract selected data types.

    The Excel contains rows like:
      Available Data Types.Pathogen Genomic -> Yes/No
      Available Data Types.Socio-demographic -> Yes/No

    Only fields marked as Yes are included in dataSummary.typeOfData.
    """
    prefix = "Available Data Types (Y/N - choose from dropdowns)."

    return [
        key.replace(prefix, "")
        for key, value in lookup.items()
        if key.startswith(prefix)
        and str(value).strip().lower() == "yes"
    ]


def build_person(lookup, prefix):
    """Build a single Contact-style object for contacts or investigators."""
    person = {}

    for source_key, output_key in PERSON_FIELD_MAPPINGS.items():
        value = get(lookup, f"{prefix}.{source_key}")
        if value:
            person[output_key] = value

    return person


def build_people(lookup, prefix):
    """Build one or more Contact-style objects.

    Supports the original single-person fields:
      investigators.name

    Also supports indexed sections or field names:
      investigators[1].name
      investigators.name[1]
      investigators.1.name
    """
    indexed_people = {}
    field_names = "|".join(re.escape(field) for field in PERSON_FIELD_MAPPINGS)
    patterns = [
        re.compile(rf"^{re.escape(prefix)}\[(\d+)\]\.({field_names})$"),
        re.compile(rf"^{re.escape(prefix)}\.({field_names})\[(\d+)\]$"),
        re.compile(rf"^{re.escape(prefix)}\.(\d+)\.({field_names})$")
    ]

    for lookup_key, value in lookup.items():
        for pattern_index, pattern in enumerate(patterns):
            match = pattern.match(lookup_key)
            if not match:
                continue

            if pattern_index == 1:
                source_key, person_index = match.groups()
            else:
                person_index, source_key = match.groups()

            person = indexed_people.setdefault(int(person_index), {})
            person[PERSON_FIELD_MAPPINGS[source_key]] = value
            break

    people = [
        indexed_people[index]
        for index in sorted(indexed_people)
        if indexed_people[index]
    ]

    if people:
        return people

    person = build_person(lookup, prefix)
    return [person] if person else []


def build_publication(lookup):
    """Build a publication object from the publication section."""
    publication = {}

    mappings = {
        "title": "publication.title",
        "doi": "publication.DOI",
        "url": "publication.URL"
    }

    for output_key, lookup_key in mappings.items():
        value = get(lookup, lookup_key)
        if value:
            publication[output_key] = value

    return publication


def build_cohort_json(lookup, accession, project, tag, label):
    """Build the final cohort JSON object from the lookup dictionary."""
    cohort = deepcopy(EMPTY_COHORT_TEMPLATE)

    age_min = get(lookup, "Additional Metadata (aggregated).Age range (min)")
    age_max = get(lookup, "Additional Metadata (aggregated).Age range (max)")

    # Top-level cohort fields
    cohort["accession"] = accession
    cohort["cohortName"] = get(lookup, "Overview.cohortName")
    cohort["description"] = get(lookup, "Overview.description")
    cohort["acronym"] = get(lookup, "Overview.acronym")
    cohort["website"] = get(lookup, "Overview.website")
    cohort["studyDesign"] = get(lookup, "Study Design.studyDesign")

    # Provider details
    cohort["provider"]["name"] = get(lookup, "provider.name")
    cohort["provider"]["website"] = get(lookup, "Overview.website")

    # Contacts and investigators are Contact-style objects
    contacts = build_people(lookup, "contacts")
    investigators = build_people(lookup, "investigators")

    if contacts:
        cohort["contacts"] = contacts

    if investigators:
        cohort["investigators"] = investigators

    # Dates and enrollment
    cohort["startDate"] = normalize_date(get(lookup, "Study Design.startDate"))
    cohort["endDate"] = normalize_date(get(lookup, "Study Design.endDate"))
    cohort["targetEnrollment"] = to_int(get(lookup, "Study Design.targetEnrollment"))
    cohort["totalEnrollment"] = to_int(get(lookup, "Study Design.totalEnrollment"))

    # Publications
    publication = build_publication(lookup)
    cohort["publications"] = [publication] if publication else []

    # Other top-level metadata
    cohort["funding"] = get(lookup, "publication.funding organisation")
    cohort["territories"] = split_list(get(lookup, "Study Design.territories/countries"))
    cohort["project"] = project
    cohort["tags"] = [tag]
    cohort["label"] = label

    # Data summary fields
    cohort["dataSummary"]["diseases"] = split_list(
        get(lookup, "Additional Metadata (aggregated).comorbidities")
    )
    cohort["dataSummary"]["medication"] = split_list(
        get(lookup, "Additional Metadata (aggregated).medication")
    )
    cohort["dataSummary"]["treatment"] = split_list(
        get(lookup, "Additional Metadata (aggregated).treatment")
    )
    cohort["dataSummary"]["outcome"] = split_list(
        get(lookup, "Additional Metadata (aggregated).outcome")
    )
    cohort["dataSummary"]["complications"] = split_list(
        get(lookup, "Additional Metadata (aggregated).complications")
    )
    cohort["dataSummary"]["typeOfData"] = get_type_of_data(lookup)

    cohort["dataSummary"]["ageRange"] = (
        f"{age_min} - {age_max}" if age_min and age_max else ""
    )

    cohort["dataSummary"]["sampleSize"] = to_int(
        get(lookup, "Study Design.totalEnrollment")
    )
    cohort["dataSummary"]["sampleType"] = get(
        lookup, "Additional Metadata (aggregated).sample types"
    )
    cohort["dataSummary"]["followUpSchedule"] = get(
        lookup, "Study Design.follow-up schedule"
    )
    cohort["dataSummary"]["inclusionCriteria"] = get(
        lookup, "Study Design.population"
    )
    cohort["dataSummary"]["taxId"] = split_list(get(lookup, "Overview.taxID"))
    cohort["dataSummary"]["hostTaxId"] = split_list(get(lookup, "Overview.hostTaxID"))

    return cohort


def find_missing_template_keys(obj, template, path=""):
    """Recursively check that the generated JSON has all template keys."""
    missing = []

    for key, template_value in template.items():
        current_path = f"{path}.{key}" if path else key

        if key not in obj:
            missing.append(current_path)
            continue

        if isinstance(template_value, dict):
            if not isinstance(obj[key], dict):
                missing.append(current_path)
            else:
                missing.extend(
                    find_missing_template_keys(obj[key], template_value, current_path)
                )

    return missing


def validate_against_template(cohort):
    """Validate generated JSON against EMPTY_COHORT_TEMPLATE structure."""
    missing_keys = find_missing_template_keys(cohort, EMPTY_COHORT_TEMPLATE)

    if missing_keys:
        raise ValueError(
            "Generated cohort JSON is missing template keys: "
            + ", ".join(missing_keys)
        )


def validate_required_values(cohort):
    """Validate important required values before writing output."""
    required_fields = [
        "accession",
        "cohortName",
        "description",
        "type",
        "project",
        "label",
        "_class"
    ]

    missing_values = []

    for field in required_fields:
        if not cohort.get(field):
            missing_values.append(field)

    if missing_values:
        raise ValueError(
            "Missing required values: " + ", ".join(missing_values)
        )

    # Warnings do not stop the script, but they help review the output
    if not cohort.get("territories"):
        print("Warning: territories is empty")

    if not cohort.get("tags"):
        print("Warning: tags is empty")

    if not cohort["dataSummary"].get("typeOfData"):
        print("Warning: dataSummary.typeOfData is empty")


def append_to_schema(new_cohort, schema_path, output_path):
    """Append the new cohort object to init_db_schema.json.

    This function does not overwrite the original schema.
    It writes a new updated file, usually:
      output/init_db_schema.updated.json
    """
    schema_path = Path(schema_path)
    output_path = Path(output_path)

    with open(schema_path, "r", encoding="utf-8") as f:
        schema = json.load(f)

    if not isinstance(schema, list):
        raise ValueError(
            "Expected init_db_schema.json to contain a JSON list of cohorts"
        )

    # Prevent accidentally adding the same accession twice
    existing_accessions = {
        cohort.get("accession")
        for cohort in schema
        if isinstance(cohort, dict)
    }

    if new_cohort["accession"] in existing_accessions:
        raise ValueError(
            f"Accession already exists in schema: {new_cohort['accession']}"
        )

    schema.append(new_cohort)

    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(schema, f, indent=2, ensure_ascii=False)

    print(f"Created updated schema file: {output_path}")


def main():
    parser = argparse.ArgumentParser(
        description="Convert Cohort Atlas Excel metadata template into JSON"
    )

    parser.add_argument("--excel", required=True)
    parser.add_argument("--accession", required=True)
    parser.add_argument("--project", required=True)
    parser.add_argument("--tag", required=True)
    parser.add_argument("--label", required=True)

    parser.add_argument(
        "--output",
        default="output/cohort.json",
        help="Path for the generated single cohort JSON file"
    )

    parser.add_argument(
        "--schema",
        help="Optional path to init_db_schema.json. If provided, the new cohort is appended to a generated updated schema file."
    )

    parser.add_argument(
        "--schema-output",
        default="output/init_db_schema.updated.json",
        help="Path for the generated updated schema JSON file"
    )

    args = parser.parse_args()

    # Read the Excel template
    df = pd.read_excel(args.excel, sheet_name="TODO")

    if VALUE_COLUMN not in df.columns:
        raise ValueError(f"Column '{VALUE_COLUMN}' not found")

    # Stage 1: Excel -> lookup dictionary
    lookup = build_lookup(df, VALUE_COLUMN)

    # Stage 2: lookup dictionary -> cohort JSON
    cohort_json = build_cohort_json(
        lookup=lookup,
        accession=args.accession,
        project=args.project,
        tag=args.tag,
        label=args.label
    )

    # Validate before writing anything
    validate_against_template(cohort_json)
    validate_required_values(cohort_json)

    # Write the single generated cohort JSON
    output_path = Path(args.output)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(cohort_json, f, indent=2, ensure_ascii=False)

    print(f"Created cohort JSON file: {output_path}")

    # Optional: append the new cohort to the full init_db_schema.json
    if args.schema:
        append_to_schema(
            new_cohort=cohort_json,
            schema_path=args.schema,
            output_path=args.schema_output
        )


if __name__ == "__main__":
    main()
