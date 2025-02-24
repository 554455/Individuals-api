package com.umaraliev.individualsapi.configuration;

import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.admin.client-id}")
    private String clientIdAdmin;
    @Value("${app.keycloak.admin.client-secret}")
    private String clientSecretAdmin;
    @Value("${app.keycloak.admin.username}")
    private String username;
    @Value("${app.keycloak.admin.password}")
    private String password;
    @Value("${app.keycloak.realm}")
    private String realm;
    @Value("${app.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${app.keycloak.user-auth-tokens.client-id}")
    private String clientIdUserAuthTokens;
    @Value("${app.keycloak.user-auth-tokens.client-secret}")
    private String clientSecretUserAuthTokens;


    @Value("${app.keycloak.user-exchange-tokens.client-id}")
    private String clientIdUserExchangeTokens;
    @Value("${app.keycloak.user-exchange-tokens.client-secret}")
    private String clientUserExchangeTokens;


    @Bean
    public Keycloak keycloak(){

        return KeycloakBuilder.builder()
                .clientSecret(clientSecretAdmin)
                .clientId(clientIdAdmin)
                .grantType("client_credentials")
                .realm(realm)
                .username(username)
                .password(password)
                .serverUrl(serverUrl)
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
                .build();
    }

    public Keycloak KeycloakUserTokens(UserAuthTokenDTO userAuthTokenDTO){
        return KeycloakBuilder.builder()
                .clientSecret(clientSecretUserAuthTokens)
                .clientId(clientIdUserAuthTokens)
                .grantType("password")
                .realm(realm)
                .username(userAuthTokenDTO.getUsername())
                .password(userAuthTokenDTO.getPassword())
                .serverUrl(serverUrl)
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
                .build();
    }

    public Keycloak keycloakTokenExchange() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType("refresh_token")
                .clientId(clientIdUserExchangeTokens)
                .clientSecret(clientUserExchangeTokens)
                .build();
    }



    private final RestTemplate restTemplate = new RestTemplate();

    public String refreshAccessToken(String refreshToken) {
        String url = "http://localhost:8080/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientIdUserExchangeTokens);
        body.add("client_secret", clientUserExchangeTokens);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});

        return response.getBody().get("access_token").toString();
    }


}
