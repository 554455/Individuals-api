package com.umaraliev.individualsapi.controllers;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class KeycloakClientTest {

    @Container
    KeycloakContainer keycloak =
            new KeycloakContainer("quay.io/keycloak/keycloak:26.0.6")
                    .withAdminUsername("admin")
                    .withAdminPassword("admin")
                    .withRealmImportFile("src/test/resources/realm.json");

    public void userNewRegistrationTest() {
        Keycloak keycloakAdminClient = KeycloakBuilder.builder()
                .serverUrl(keycloak.getAuthServerUrl())
                .realm(KeycloakContainer.MASTER_REALM)
                .clientId(KeycloakContainer.ADMIN_CLI_CLIENT)
                .username(keycloak.getAdminUsername())
                .password(keycloak.getAdminPassword())
                .build();

        
    }

}
