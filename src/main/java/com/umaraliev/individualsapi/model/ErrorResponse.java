package com.umaraliev.individualsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse{
    private String errorCode;
    private String errormessage;
    private LocalDateTime timestamp;
}
