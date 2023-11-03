package uk.ac.ebi.biosamples.cohortatlas;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public abstract class KeycloakTestContainers {
  static final KeycloakContainer keycloak;

  static {
    keycloak = new KeycloakContainer().withRealmImportFile("cohort-atlas-realm.json");
    keycloak.start();
  }

  @DynamicPropertySource
  static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/cohort-atlas");
    registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/cohort-atlas");
  }
}