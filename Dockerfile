FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} cohort-atlas.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/cohort-atlas.jar"]
