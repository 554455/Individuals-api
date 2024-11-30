package com.umaraliev.individualsapi.controllers;


import com.umaraliev.individualsapi.configuration.KeycloakConfig;
import com.umaraliev.individualsapi.dto.UserAuthTokenDTO;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.service.ReceiveUserTokensService;
import com.umaraliev.individualsapi.service.TokenExchangeService;
import com.umaraliev.individualsapi.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final ReceiveUserTokensService receiveUserTokensService;
    private final TokenExchangeService tokenExchangeService;

    @PostMapping("/registration")
    public AccessTokenResponse userNewRegistration(@RequestBody User user){
        UserAuthTokenDTO userAuthTokenDTO = new UserAuthTokenDTO(user.getEmail(), user.getPassword());
        userRegistrationService.createNewUser(user);
        return receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
    }

    @GetMapping("/login")
    public AccessTokenResponse receiveUserTokens(@RequestBody UserAuthTokenDTO userAuthTokenDTO){
        return  receiveUserTokensService.receiveUserTokens(userAuthTokenDTO);
    }

    @GetMapping("/exchange")
    public String exchangeToken(){
        String refreshToken = "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzYjcyNjkzNi01NDE1LTRhM2ItOTNlOS0wM2NlNWJiNGFiYzQifQ.eyJleHAiOjE3MzI5NjE0NzMsImlhdCI6MTczMjk1OTY3MywianRpIjoiYzI2OTRlODYtOGUwNy00YWYwLTgyYTQtMmIwMWY2OWIxNTkwIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvcmVhbG1zL21hc3RlciIsInN1YiI6IjEwMGY0N2NkLTllZmUtNDIwNC04ZDFmLTRmMDRhYTFjZDdlMyIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJ1c2VyLWF1dGgtdG9rZW5zIiwic2lkIjoiNTVlNWU0NjItZjUyYS00MWJiLTk1N2YtNjZmNzc4YWMyZjEzIiwic2NvcGUiOiJwcm9maWxlIHJvbGVzIHdlYi1vcmlnaW5zIGVtYWlsIGFjciBiYXNpYyJ9.foCen1CFdsaCINxX0QqPXnKOs-NdobhI-wmW2GIdIaDQ74BKyuO0B0TM0v6YZqi7BdfQREMeU5HbQ_YaK61AMA";
        return tokenExchangeService.exchangeToken(refreshToken);
    }
}
