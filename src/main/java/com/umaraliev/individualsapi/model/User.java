package com.umaraliev.individualsapi.model;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class User {

    public UUID id;
    public String secretKey;
    public String email;

}
