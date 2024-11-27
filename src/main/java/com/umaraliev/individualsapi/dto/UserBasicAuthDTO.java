package com.umaraliev.individualsapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserBasicAuthDTO {
    private String username;
    private String password;
}
