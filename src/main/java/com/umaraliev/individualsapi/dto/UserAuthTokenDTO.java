package com.umaraliev.individualsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserAuthTokenDTO{
    public UUID id;
    public String secretKey;
    public String email;
}
