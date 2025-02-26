package com.umaraliev.individualsapi.service;

import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.individualsapi.dto.AuthTokenResponse;
import com.umaraliev.individualsapi.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

    private final KeycloakClientService keycloakClientService;
    private final PersonClientService personClientService;

    public AuthTokenResponse createNewUser(IndividualDTO individualDTO) {
        log.info("Request to save an individual in the person-api microservice" + individualDTO);
        User user = personClientService.requestRegistrationUserPersonAPI(individualDTO);

        return keycloakClientService.createNewUser(user);
    }
}
