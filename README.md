# cohort-atlas
A browser for discovering cohorts and related resources

## Requirements
- Java 17
- docker (23.0.1) and docker compose (v2.16.0)

## Build and run
```shell
./gradlew build
docker compose build
docker compose up
```

### Build without integration tests
```shell
./gradlew build -x check
```
