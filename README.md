# cohort-atlas
A browser for discovering cohorts and related resources

To run the project follow these steps:

1) Build the project using the commands
```shell
gradle build -x check
./gradlew build --args='--spring.profiles.active=test'
```

2) Build the Docker image using
```shell
docker build -t cohort-atlas .
```

3) Start the Docker container using
```shell
docker-compose up
```

4) Access to Cohort Atlas will be by: http://localhost:8081/

In addition, check the Java version (17th is expected) and update if necessary:

```shell
/usr/libexec/java_home

echo "export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home" >> ~/.bash_profile

source ~/.bash_profile

echo $JAVA_HOME
```
