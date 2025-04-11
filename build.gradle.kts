plugins {
	java
	`jvm-test-suite`
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "uk.ac.ebi.biosamples"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
	implementation ("org.springframework.data:spring-data-rest-hal-browser:3.3.9.RELEASE")
	implementation ("org.springframework.data:spring-data-rest-hal-explorer")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation ("org.springframework.security:spring-security-oauth2-jose:6.1.4")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.7.8")

//	implementation("dev.langchain4j:langchain4j-spring-boot-starter:1.0.0-beta2")
//	implementation("dev.langchain4j:langchain4j-google-ai-gemini-spring-boot-starter:1.0.0-beta2")
	implementation("dev.langchain4j:langchain4j-google-ai-gemini:1.0.0-beta2")

//	implementation("com.opencsv:opencsv:5.7.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.14.2")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.6.1")
	testImplementation("org.testcontainers:testcontainers:1.19.1")
	testImplementation("com.github.dasniko:testcontainers-keycloak:3.0.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

testing {
	suites {
		val test by getting(JvmTestSuite::class) {
			useJUnitJupiter()
		}

		val integrationTest by registering(JvmTestSuite::class) {
			dependencies {
				implementation(project())
				implementation("org.springframework.boot:spring-boot-starter-test")
				implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.6.1")
				implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
				implementation("org.springframework.boot:spring-boot-starter-hateoas")
				implementation("org.springframework.boot:spring-boot-starter-web")

				implementation("org.junit.jupiter:junit-jupiter:5.8.1")
				implementation("org.testcontainers:junit-jupiter:1.19.1")
				implementation("org.testcontainers:testcontainers:1.19.1")
				implementation("com.github.dasniko:testcontainers-keycloak:3.0.0")
			}

//			sources {
//				java {
//					setSrcDirs(listOf("src/integrationTest/java"))
//				}
//			}

			targets {
				all {
					testTask.configure {
						shouldRunAfter(test)
					}
				}
			}
		}
	}
}

tasks.named("check") {
	dependsOn(testing.suites.named("integrationTest"))
}
