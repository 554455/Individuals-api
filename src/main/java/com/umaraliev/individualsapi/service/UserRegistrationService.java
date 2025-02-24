package com.umaraliev.individualsapi.service;


import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.model.NewUserRecord;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.Token;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRegistrationService implements UserRepository{

    @Value("${app.keycloak.realm}")
    private String realm;

    private final KeycloakConfig keycloakConfig;

    private final RequestPersonAPIService requestPersonAPIService;

    @Override
    public String createNewUser(User user) {
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

        String token = keycloakConfig.keycloak().tokenManager().getAccessToken().getToken();
        Response response;
        try {
            response = keycloakConfig.keycloak().realm(realm).users().create(userRepresentation);
        }catch (Exception e){
            return "User with:" + user.getEmail() + " already registered";
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        if (userId == null) {
            requestPersonAPIService.requestRemoveUserPersonAPI(user);
            return new RuntimeException("User with:" + user.getEmail() + " already registered").toString();
        }

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.secretKey);

        keycloakConfig.keycloak().realm(realm).users().get(userId).resetPassword(credentialRepresentation);
        System.out.println(token);

        return token;
    }
}
