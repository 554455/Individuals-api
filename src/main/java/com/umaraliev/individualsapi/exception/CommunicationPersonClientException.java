package com.umaraliev.individualsapi.exception;

public class CommunicationPersonClientException extends RuntimeException {
    public CommunicationPersonClientException(String message) {
        super(message);
    }
    public CommunicationPersonClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
