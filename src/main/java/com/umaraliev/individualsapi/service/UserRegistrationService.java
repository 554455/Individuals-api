package com.umaraliev.individualsapi.service;


import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.model.NewUserRecord;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.Token;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService implements UserRepository{

    @Value("${app.keycloak.realm}")
    private String realm;

    private final KeycloakConfig keycloakConfig;

    private final RequestPersonAPIService requestPersonAPIService;

    @Override
    public String createNewUser(User user) {

        try {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEmailVerified(false);
            userRepresentation.setEnabled(true);
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setUsername(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("global_id", List.of(user.getId().toString()));
            userRepresentation.setAttributes(attributes);


            log.info("Request to create a new user in the keycloak + userRepresentation {}", userRepresentation);
            Response response = keycloakConfig.keycloak().realm(realm).users().create(userRepresentation);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(user.secretKey);

            log.info("Request to keycloak to change passwords + user.secretKey {}", user.secretKey);
            keycloakConfig.keycloak()
                    .realm(realm)
                    .users()
                    .get(response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"))
                    .resetPassword(credentialRepresentation);

        } catch (Exception e) {
            requestPersonAPIService.requestRemoveUserPersonAPI(user);
            throw new RuntimeException("An error occurred while saving the user in keycloak" + e.getMessage());
        }
        return "User created successfully";
    }
}
