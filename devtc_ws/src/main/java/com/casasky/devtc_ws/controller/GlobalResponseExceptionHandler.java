package com.casasky.devtc_ws.controller;


import static java.util.stream.Collectors.joining;

import java.util.stream.Collectors;

import com.casasky.devtc_ws.service.exception.GlobalRuntimeException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalResponseExceptionHandler {

    @ExceptionHandler(GlobalRuntimeException.class)
    public ResponseEntity<?> handle(GlobalRuntimeException e) {
        return ResponseEntity.unprocessableEntity()
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.unprocessableEntity()
                .body(e.getBindingResult().getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collect(Collectors.toUnmodifiableList()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(HttpMessageNotReadableException e) {
        var cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            var invalidFormatException = (InvalidFormatException) cause;
            return ResponseEntity.unprocessableEntity()
                    .body(extractField(invalidFormatException) + " must be from type " + extractMessage(invalidFormatException));
        }
        return ResponseEntity.unprocessableEntity()
                .body(cause.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    private String extractField(InvalidFormatException e) {
        return e.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(joining("."));

    }

    private String extractMessage(InvalidFormatException e) {
        return e.getTargetType().getSimpleName();
    }

}
