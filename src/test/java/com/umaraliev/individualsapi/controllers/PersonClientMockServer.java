package com.umaraliev.individualsapi.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.umaraliev.individualsapi.model.User;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonApiMockServer {

    private static final int WIREMOCK_PORT = 8081;
    private static WireMockServer wireMockServer;

    public PersonApiMockServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(WIREMOCK_PORT);
            wireMockServer.start();
            configurePersonApiMocks();
        }
    }

    private void configurePersonApiMocks() {
        WireMock.configureFor("localhost", WIREMOCK_PORT);
        // Mock для POST /api/v1/auth/registration
        WireMock.stubFor(post(urlPathEqualTo("/api/v1/auth/registration"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"secretKey\": \"secret\", \"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}")));

        // Mock для PUT /api/v1/auth/update/{id}
        WireMock.stubFor(WireMock.put(WireMock.urlPathMatching("/api/v1/auth/update/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"secretKey\": \"updated-secret\", \"email\": \"updated@example.com\", \"firstName\": \"Jane\", \"lastName\": \"Doe\"}")));
    }


    public void verifyRemoveUserEndpointCalled(User user) {
        List<LoggedRequest> requests = WireMock.findAll(postRequestedFor(urlPathEqualTo("/api/v1/auth/remove"))
                .withRequestBody(matchingJsonPath("$.id", equalTo(user.getId().toString()))));

        assertEquals(1, requests.size(), "Expected one POST request to /api/v1/auth/remove with id " + user.getId());
    }


    public void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
