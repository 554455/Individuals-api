package com.umaraliev.individualsapi.controllers;


import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.service.ReceiveUserTokensService;
import com.umaraliev.individualsapi.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final ReceiveUserTokensService receiveUserTokensService;

    @PostMapping("/registration")
    public String userNewRegistration(@RequestBody User user){
        return userRegistrationService.createNewUser(user);
    }

    @GetMapping("/login")
    public AccessTokenResponse hello(@RequestBody UserAuthTokenDTO userAuthTokenDTO){
        return  receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
    }
}
