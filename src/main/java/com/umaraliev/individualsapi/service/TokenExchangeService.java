package com.umaraliev.individualsapi.service;

import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import jakarta.ws.rs.core.Form;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenExchangeService {

    private final KeycloakConfig keycloakConfig;

    public String exchangeToken(String refreshToken) {
        return keycloakConfig.refreshAccessToken(refreshToken);
    }
}
