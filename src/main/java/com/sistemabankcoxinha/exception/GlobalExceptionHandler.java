package com.sistemabankcoxinha.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> tratarRuntimeException(
            RuntimeException ex) {

        ErrorResponse erro =
                new ErrorResponse(ex.getMessage());

        return new ResponseEntity<>(
                erro,
                HttpStatus.BAD_REQUEST
        );
    }
}