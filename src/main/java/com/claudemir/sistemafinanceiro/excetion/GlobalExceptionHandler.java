package com.claudemir.sistemafinanceiro.excetion;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<String> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        String message = "Campo(s) inválido(s) na requisição: " + ex.getPropertyName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
