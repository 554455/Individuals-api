package com.umaraliev.individualsapi.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {

    String username;
    String password;
    String firstName;
    String lastName;
    String email;
}
