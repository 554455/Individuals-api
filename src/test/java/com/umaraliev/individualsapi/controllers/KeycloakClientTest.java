package com.umaraliev.individualsapi.controllers;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class KeycloakClientTest {

    @LocalServerPort
    private int port;

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.0.6")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withRealmImportFile("src/test/resources/realm.json");

    private Keycloak keycloakClient;
    private PersonApiMockServer personApiMockServer;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/master");
        registry.add("app.keycloak.serverUrl", keycloak::getAuthServerUrl);
        registry.add("person.api.registration-url", () -> "http://localhost:8081/api/v1/auth/registration");
        registry.add("person.api.remove-url", () -> "http://localhost:8081/api/v1/auth/remove");
    }

    @BeforeEach
    void setUp() {
        keycloakClient = keycloak.getKeycloakAdminClient();
        personApiMockServer = new PersonApiMockServer();
    }
}
