package com.umaraliev.individualsapi.controllers;

import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.individualsapi.dto.AuthTokenResponse;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ReceiveUserTokensService receiveUserTokensService;
    private final TokenExchangeService tokenExchangeService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/registration")
    public AuthTokenResponse userNewRegistration(@RequestBody IndividualDTO individualDTO){
        return userRegistrationService.createNewUser(individualDTO);
    }

    @GetMapping("/login")
    public AccessTokenResponse receiveUserTokens(@RequestBody UserAuthTokenDTO userAuthTokenDTO){
        return  receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
    }

    @GetMapping("/exchange")
    public String exchangeToken(String refreshToken){
        return tokenExchangeService.exchangeToken(refreshToken);
    }
}
