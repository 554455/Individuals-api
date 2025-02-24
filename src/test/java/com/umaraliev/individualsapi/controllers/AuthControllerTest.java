package com.umaraliev.individualsapi.controllers;

import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.common.dto.ResponseIndividualDTO;
import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.service.ReceiveUserTokensService;
import com.umaraliev.individualsapi.service.RequestPersonAPIService;
import com.umaraliev.individualsapi.service.TokenExchangeService;
import com.umaraliev.individualsapi.service.UserRegistrationService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestTemplate;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private ReceiveUserTokensService receiveUserTokensService;

    @Autowired
    private RequestPersonAPIService requestPersonAPIService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.admin.client-id}")
    private String clientIdAdmin;

    @Value("${app.keycloak.admin.client-secret}")
    private String clientSecretAdmin;

    @Value("${spring.security.oauth2.resourceserver.jwt.introspect-uri}")
    private String serverUrl;

    public static IndividualDTO createTestIndividualDTO() {
        IndividualDTO individualDTO = new IndividualDTO();
        individualDTO.setPassportNumber("P1234567");
        individualDTO.setPhoneNumber("555-1234");

        IndividualDTO.UserDTO user = new IndividualDTO.UserDTO();
        user.setEmail("test989@example.com");
        user.setSecretKey("secret123");
        user.setFirstName("John");
        user.setLastName("Doe");

        IndividualDTO.AddressDTO address = new IndividualDTO.AddressDTO();
        address.setAddress("123 Main St");
        address.setCity("Test City");
        address.setState("Test State");
        address.setZipCode("12345");

        IndividualDTO.CountryDTO country = new IndividualDTO.CountryDTO();
        country.setName("Test Country");
        address.setCountry(country);

        user.setAddress(address);
        individualDTO.setUser(user);

        return individualDTO;
    }

    @Test
    void testUserNewRegistration() {
       User user = requestPersonAPIService.requestRegistrationUserPersonAPI(createTestIndividualDTO());
       assertNotNull(user);
       assertEquals(user.getEmail(), createTestIndividualDTO().getUser().getEmail());
       assertEquals(user.getSecretKey(), createTestIndividualDTO().getUser().getSecretKey());

       UserAuthTokenDTO userAuthTokenDTO = new UserAuthTokenDTO(user.getEmail(), user.secretKey);
       String token = userRegistrationService.createNewUser(user);
       assertNotNull(token);

        List<UserRepresentation> users = keycloakConfig.keycloak().realm(realm).users().searchByEmail(user.getEmail(), true);
        assertEquals(user.getEmail(), users.get(0).getEmail());

       AccessTokenResponse accessTokenResponse = receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
       assertNotNull(accessTokenResponse);

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("client_id", clientIdAdmin);
        params.put("client_secret", clientSecretAdmin);

        ResponseEntity<Map> response = restTemplate.postForEntity(serverUrl, params, Map.class);
        assertEquals(response.getBody().containsKey("active"), true);

    }

    @Test
    void testUserLogin() {
        UserAuthTokenDTO userAuthTokenDTO = new UserAuthTokenDTO("test989@example.com", "secret123");
        AccessTokenResponse accessTokenResponse = receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);

        assertNotNull(accessTokenResponse);
        Map<String, String> params = new HashMap<>();
        params.put("token", accessTokenResponse.getToken());
        params.put("client_id", clientIdAdmin);
        params.put("client_secret", clientSecretAdmin);

        ResponseEntity<Map> response = restTemplate.postForEntity(serverUrl, params, Map.class);
        assertEquals(response.getBody().containsKey("active"), true);
    }
}