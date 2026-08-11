package com.adps.e_commerce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RegradeNegocioException.class)
    public ResponseEntity<Map<String, String>> handleRegraDeNegocioException(RegradeNegocioException ex){
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
