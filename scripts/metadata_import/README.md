# Cohort Metadata Import Script

This script converts a Cohort Atlas Excel metadata template into a JSON cohort object and can optionally append it to `init_db_schema.json`.

## Location

```text
scripts/metadata_import/excel_to_schema.py
```

---

# Requirements

Create and activate a Python virtual environment from the repository root:

```bash
python3 -m venv .venv
source .venv/bin/activate
```

Install dependencies:

```bash
pip install -r scripts/metadata_import/requirements.txt
```

`requirements.txt`:

```text
pandas
openpyxl
pyyaml
```

---

# Input Excel format

The Excel file must contain a sheet based on the Cohort Metadata template.

The script expects a value column:

```text
#Value(TODO)
```

The metadata structure is built from:

```text
#FieldType
#FieldName
#Value(TODO)
```

Example:

| #FieldType | #FieldName | #Value(TODO)                         |
| ---------- | ---------- | ------------------------------------ |
| Overview   | cohortName | International Research Consortium... |

becomes:

```json
{
  "cohortName": "International Research Consortium..."
}
```

## Multiple contacts or investigators

The original single-person format is still supported:

| #FieldType     | #FieldName | #Value(TODO)          |
| -------------- | ---------- | --------------------- |
| investigators  | name       | Dr Jane Example       |
|                | email      | jane@example.org      |

To add multiple investigators, repeat the person fields:

| #FieldType        | #FieldName | #Value(TODO)          |
| ----------------- | ---------- | --------------------- |
| investigators     | name       | Dr Jane Example       |
|                   | email      | jane@example.org      |
|                   | name       | Dr Alex Example       |
|                   | email      | alex@example.org      |

You can also index either the section or the field name explicitly:

| #FieldType        | #FieldName | #Value(TODO)          |
| ----------------- | ---------- | --------------------- |
| investigators[1]  | name       | Dr Jane Example       |
| investigators[1]  | email      | jane@example.org      |
| investigators[2]  | name       | Dr Alex Example       |
| investigators[2]  | email      | alex@example.org      |

This produces:

```json
{
  "investigators": [
    {
      "name": "Dr Jane Example",
      "email": "jane@example.org"
    },
    {
      "name": "Dr Alex Example",
      "email": "alex@example.org"
    }
  ]
}
```

## Date format

Imported `startDate` and `endDate` values are normalized when possible:

- Full dates are written as `YYYY-MM-DD`.
- Year-only dates are kept as `YYYY`.
- Non-date text such as `Ongoing` is kept unchanged.

---

# Workflow

The script follows a two-stage transformation:

```text
Excel template
      ↓
lookup dictionary
      ↓
cohort JSON
      ↓
(optional)
updated init_db_schema.json
```

Separating the lookup dictionary from the final JSON makes the code easier to maintain.

---

# Generate a cohort JSON file

```bash
python scripts/metadata_import/excel_to_schema.py \
  --excel CohortBrowserMetadata.xlsx \
  --accession BSC00000XX \
  --project ProjectName \
  --tag ReCoDID \
  --label RECODID \
  --output output/cohort_name.json
```

Output:

```text
output/cohort_name.json
```

---

# Generate an updated init_db_schema.json

To append the new cohort to the current schema:

```bash
python scripts/metadata_import/excel_to_schema.py \
  --excel CohortBrowserMetadata.xlsx \
  --accession BSC00000XX \
  --project ProjectNAme \
  --tag ReCoDID \
  --label RECODID \
  --output output/cohort_name.json \
  --schema src/main/resources/env/init_db_schema.json \
  --schema-output output/init_db_schema.updated.json
```

Generated files:

```text
output/
├── cohort_name.json
└── init_db_schema.updated.json
```

The original schema is **never modified automatically**.

---

# Review generated schema

Compare the original and updated schema:

```bash
diff src/main/resources/env/init_db_schema.json \
     output/init_db_schema.updated.json
```

Or open both in VS Code:

```bash
code src/main/resources/env/init_db_schema.json
code output/init_db_schema.updated.json
```

---

# Replace the schema after review

Once the generated schema has been verified:

```bash
cp output/init_db_schema.updated.json \
   src/main/resources/env/init_db_schema.json
```

Inspect the changes:

```bash
git diff src/main/resources/env/init_db_schema.json
```

---

# Validation

The script validates:

- Required fields exist.
- Generated JSON follows the cohort template structure.
- Accessions are unique.
- Important warnings are displayed for empty optional fields.

Required fields:

- accession
- cohortName
- description
- project
- label
- type
- `_class`

---

# Recommended PR workflow

## Create a feature branch

```bash
git checkout main
git pull origin main
git checkout -b feature/contagio-metadata-import
```

---

## Run the importer

```bash
python scripts/metadata_import/excel_to_schema.py \
  --excel metadata.xlsx \
  --accession BSC00000XX \
  --project ProjectName \
  --tag ReCoDID \
  --label RECODID \
  --schema src/main/resources/env/init_db_schema.json
```

---

## Review generated files

```bash
diff src/main/resources/env/init_db_schema.json \
     output/init_db_schema.updated.json
```

---

## Replace the schema

```bash
cp output/init_db_schema.updated.json \
   src/main/resources/env/init_db_schema.json
```

---

## Review Git changes

```bash
git status
git diff
```

---

## Add files to the commit

```bash
git add src/main/resources/env/init_db_schema.json
```

---

## Ignore temporary output files

Add to `.gitignore`:

```gitignore
output/
*.updated.json
```

Do **not** commit:

```text
output/cohort_name.json
output/init_db_schema.updated.json
```

---

## Commit

```bash
git commit -m "Add cohort metadata importer and update init DB schema"
```

---

## Push branch

```bash
git push origin feature/contagio-metadata-import
```

Open a Pull Request against `main`.

---

# PR Checklist

Before opening the PR:

- [ ] Generated cohort JSON reviewed.
- [ ] Updated schema reviewed.
- [ ] `init_db_schema.json` intentionally updated.
- [ ] No files under `output/` committed.
- [ ] Accession is unique.
- [ ] JSON follows the Java Cohort model.
- [ ] Script runs successfully from the repository root.

---
