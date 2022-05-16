package com.challenge.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Slf4j
@ResponseBody
@Hidden
@ControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> notAcceptable(HttpServletRequest request,
                                                HttpMediaTypeNotAcceptableException exception) {
        log.warn("Handle HttpMediaTypeNotAcceptableException", exception);

        return ResponseEntity.status(NOT_ACCEPTABLE)
                .contentType(MediaType.TEXT_PLAIN)
                .body(MediaType.APPLICATION_JSON_VALUE);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(
            HttpServletRequest request, EntityNotFoundException exception) {
        log.warn("Handle EntityNotFoundException", exception);
        return ResponseEntity.status(NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> httpMethodNotSupportedException(
            HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        log.warn("Handle HttpRequestMethodNotSupportedException", exception);

        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> httpMediaTypeNotSupported(
            HttpServletRequest request, HttpMediaTypeNotSupportedException exception) {
        log.warn("Handle HttpMediaTypeNotSupportedException", exception);

        return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> generalThrowable(HttpServletRequest request, Throwable exception) {
        log.warn("Handle Throwable", exception);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }
}
