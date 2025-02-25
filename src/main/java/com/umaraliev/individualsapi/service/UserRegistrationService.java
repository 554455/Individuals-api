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

    private final UserRepositoryImpl userRepositoryImpl;
    private final RequestPersonAPIClientService requestPersonAPIClientService;

    public AuthTokenResponse createNewUser(IndividualDTO individualDTO) {
        log.info("Request to save an individual in the person-api microservice" + individualDTO);
        User user = requestPersonAPIClientService.requestRegistrationUserPersonAPI(individualDTO);

        return userRepositoryImpl.createNewUser(user);
    }
}
