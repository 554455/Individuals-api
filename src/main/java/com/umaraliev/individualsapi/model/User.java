package com.umaraliev.individualsapi.model;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
@ToString
public class User {
    public UUID id;
    public String secretKey;
    public String email;
    public String firstName;
    public String lastName;
}
