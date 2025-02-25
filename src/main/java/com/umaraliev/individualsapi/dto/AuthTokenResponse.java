package com.umaraliev.individualsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponse {

    private String accessToken;
    private long expiresIn;
    private String refreshToken;
    private long refreshExpiresIn;
}
