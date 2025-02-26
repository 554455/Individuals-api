package com.umaraliev.individualsapi.exception;

public class KeycloakTimeoutException extends RuntimeException {
    public KeycloakTimeoutException(String message) {
        super(message);
    }
    public KeycloakTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
