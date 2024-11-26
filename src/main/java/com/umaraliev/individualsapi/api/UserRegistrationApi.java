package com.umaraliev.individualsapi.api;


import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.keycloak.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UserRegistrationApi {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/registration")
    public Token userNewRegistration(@RequestBody User user){
        userRegistrationService.createNewUser(user);
        return null;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
