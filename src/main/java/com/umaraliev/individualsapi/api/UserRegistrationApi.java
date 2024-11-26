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
    public String userNewRegistration(@RequestBody User user){

        return userRegistrationService.createNewUser(user);
    }

    @GetMapping("/hello")
    public String hello() {

        User user = new User();
        user.setConfirm_password("123456");
        user.setEmail("rerrrtrrtrer@example.com");
        user.setPassword("123456");

        ;
        return userRegistrationService.createNewUser(user);
    }
}
