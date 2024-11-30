package com.umaraliev.individualsapi.model;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {

    String confirm_password;
    String password;
    @Email
    String email;


}
