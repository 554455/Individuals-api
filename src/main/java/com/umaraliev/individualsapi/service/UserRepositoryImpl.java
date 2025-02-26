package com.umaraliev.individualsapi.service;


import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.dto.AuthTokenResponse;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.repository.UserRepository;
import exception.KeycloakTimeoutException;
import exception.PasswordChangeException;
import exception.TokenRetrievalException;
import exception.UserCreationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
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
public class UserRepositoryImpl implements UserRepository{

    @Value("${app.keycloak.realm}")
    private String realm;

    private final KeycloakConfig keycloakConfig;

    private final ReceiveUserTokensService receiveUserTokensService;
    private final RequestPersonAPIClientService requestPersonAPIClientService;

    @Override
    public AuthTokenResponse createNewUser(User user) {

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

            log.info("Request to keycloak to change passwords + user.secretKey {}", user.secretKey);
            restPasswordUser(response, user);

            log.info("Request to receive tokens + user.email {}", user.getEmail());
            AuthTokenResponse authTokenResponse = getUserTokens(user);

            return authTokenResponse;
        }catch (UserCreationException e) {
            requestPersonAPIClientService.requestRemoveUserPersonAPI(user);
            throw new UserCreationException("User creation error", e);
        }



    }

    public void restPasswordUser(Response response, User user) {

        try {
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
        } catch (PasswordChangeException e) {
            throw new PasswordChangeException("Password change error", e);
        }
    }

    public AuthTokenResponse getUserTokens(User user) {
        try {
            UserAuthTokenDTO userAuthTokenDTO = new UserAuthTokenDTO(user.getEmail(), user.secretKey);
            AccessTokenResponse accessTokenResponse = receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
            AuthTokenResponse authTokenResponse = new AuthTokenResponse(
                    accessTokenResponse.getToken(),
                    accessTokenResponse.getExpiresIn(),
                    accessTokenResponse.getRefreshToken(),
                    accessTokenResponse.getRefreshExpiresIn()
            );
            return authTokenResponse;
        } catch (TokenRetrievalException e) {
            throw new TokenRetrievalException("Token retrieval error", e);
        }
    }
}
