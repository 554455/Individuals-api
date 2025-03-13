package com.umaraliev.individualsapi.controllers;

import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.individualsapi.IndividualsApiApplication;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.service.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IndividualsApiApplication.class)
@Testcontainers
public class AuthControllerTest {

    @Autowired
    private  UserRegistrationService userRegistrationService;

    private PersonApiMockServer personApiMockServer;

    @BeforeEach
    void setUp() {
        personApiMockServer = new PersonApiMockServer();
    }

    public static IndividualDTO createSampleIndividualDTO() {
        IndividualDTO.CountryDTO country = new IndividualDTO.CountryDTO();
        country.setName("USA");

        IndividualDTO.AddressDTO address = new IndividualDTO.AddressDTO();
        address.setAddress("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");
        address.setCountry(country);

        IndividualDTO.UserDTO user = new IndividualDTO.UserDTO();
        user.setEmail("test@example.com");
        user.setSecretKey("secret123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress(address);

        IndividualDTO individualDTO = new IndividualDTO();
        individualDTO.setPassportNumber("A1234567");
        individualDTO.setPhoneNumber("+1234567890");
        individualDTO.setUser(user);

        return individualDTO;
    }

    @Test
    void userNewRegistration() {

        //Returns an error when saving the user to keycloak because *admin-cli* does not have the rights to create the user.
        userRegistrationService.createNewUser(createSampleIndividualDTO());

        User user = new User();
        user.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        user.setSecretKey(createSampleIndividualDTO().getUser().getSecretKey());
        user.setEmail(createSampleIndividualDTO().getUser().getEmail());
        user.setFirstName(createSampleIndividualDTO().getUser().getFirstName());
        user.setLastName(createSampleIndividualDTO().getUser().getLastName());

        //Checking that this point has been called
        personApiMockServer.verifyRemoveUserEndpointCalled(user);
    }
}
