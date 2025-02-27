package com.umaraliev.individualsapi.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.common.dto.ResponseIndividualDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonClientMockServer {

    private static WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        // Запуск WireMock сервера на порту 8081 (или любом другом свободном порту)
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8081));
        wireMockServer.start();

        // Настройка Mock-ответов для API person-api
        configurePersonApiMocks();
    }

    @AfterAll
    public static void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private static void configurePersonApiMocks() {
        // Mock для POST /api/v1/auth/registration
        wireMockServer.stubFor(
                post(urlPathEqualTo("/api/v1/auth/registration"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"secretKey\": \"secret\", \"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}"))
        );

        // Mock для PUT /api/v1/auth/update/{id}
        wireMockServer.stubFor(
                put(urlPathMatching("/api/v1/auth/update/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"secretKey\": \"updated-secret\", \"email\": \"updated@example.com\", \"firstName\": \"Jane\", \"lastName\": \"Doe\"}"))
        );

        // Mock для POST /api/v1/auth/remove/{id}
        wireMockServer.stubFor(
                post(urlPathMatching("/api/v1/auth/remove/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value()))
        );
    }

    @Test
    public void testRegistration() {
        // Подготовка запроса
        IndividualDTO request = new IndividualDTO();
        request.setPassportNumber("123456789");
        request.setPhoneNumber("+123456789");

        IndividualDTO.UserDTO userDTO = new IndividualDTO.UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setSecretKey("secret");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        request.setUser(userDTO);

        // Вызов API individual-api, который, в свою очередь, вызывает person-api
        ResponseEntity<ResponseIndividualDTO> response = restTemplate.postForEntity(
                "/individuals/registration", request, ResponseIndividualDTO.class);

        // Проверка ответа
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", response.getBody().getId().toString());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Подготовка запроса
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        IndividualDTO request = new IndividualDTO();
        request.setPassportNumber("987654321");
        request.setPhoneNumber("+987654321");

        IndividualDTO.UserDTO userDTO = new IndividualDTO.UserDTO();
        userDTO.setEmail("updated@example.com");
        userDTO.setSecretKey("updated-secret");
        userDTO.setFirstName("Jane");
        userDTO.setLastName("Doe");
        request.setUser(userDTO);

        // Вызов API individual-api, который, в свою очередь, вызывает person-api
        ResponseEntity<ResponseIndividualDTO> response = restTemplate.exchange(
                "/individuals/update/" + userId, HttpMethod.PUT, new HttpEntity<>(request), ResponseIndividualDTO.class);

        // Проверка ответа
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated@example.com", response.getBody().getEmail());
        assertEquals("Jane", response.getBody().getFirstName());
    }

    @Test
    public void testRemoveUser() {
        // Подготовка запроса
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Вызов API individual-api, который, в свою очередь, вызывает person-api
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/individuals/remove/" + userId, null, Void.class);

        // Проверка ответа
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}