package com.umaraliev.individualsapi.exception;

import com.umaraliev.individualsapi.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(PasswordChangeException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetException(PasswordChangeException e) {
        ErrorResponse errorResponse = new ErrorResponse("USER_PASSWORD_CHANGE_ERROR", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenRetrievalException.class)
    public ResponseEntity<ErrorResponse> handleTokenRetrievalException(TokenRetrievalException e) {
        ErrorResponse errorResponse = new ErrorResponse("USER_TOKEN_RETRIEVAL_ERROR", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ErrorResponse> handleUserCreationException(UserCreationException e) {
        ErrorResponse errorResponse = new ErrorResponse("USER_CREATION_ERROR", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException e) {
        ErrorResponse errorResponse = new ErrorResponse("USER_TIMEOUT_ERROR", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommunicationPersonClientException.class)
    public ResponseEntity<ErrorResponse> handleCommunicationPersonClientException(CommunicationPersonClientException e) {
        ErrorResponse errorResponse = new ErrorResponse("PERSON_CLIENT_COMMUNICATION_ERROR", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
