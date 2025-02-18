package com.umaraliev.individualsapi.service;

import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveUserTokensService {

    public final KeycloakConfig keycloakConfig;

    public AccessTokenResponse receiveUserTokens(User user) {
        return keycloakConfig.KeycloakUserTokens(user).tokenManager().getAccessToken();
    }
}
