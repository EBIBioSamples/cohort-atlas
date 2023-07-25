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

//extra["springCloudVersion"] = "2022.0.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
	implementation ("org.springframework.data:spring-data-rest-hal-browser:3.3.9.RELEASE")
	implementation ("org.springframework.data:spring-data-rest-hal-explorer")
	implementation("com.opencsv:opencsv:5.7.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.15.2")
//	implementation("org.springframework.cloud:spring-cloud-function-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.6.1")
}

//dependencyManagement {
//	imports {
//		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//	}
//}

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
