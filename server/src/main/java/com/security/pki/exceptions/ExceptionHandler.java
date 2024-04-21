package com.security.pki.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(2)
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @AllArgsConstructor
    @Data
    public static class ApiError {

        private Integer status;
        private String message;
        private String requestedUri;
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(SignerNotFoundException.class)
    public ResponseEntity<ApiError> signerNotFoundException(SignerNotFoundException ex, HttpServletRequest request) {
        ApiError message = new ApiError(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<ApiError>(message, HttpStatus.I_AM_A_TEAPOT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidSignerException.class)
    public ResponseEntity<ApiError> invalidSignerException(InvalidSignerException ex, HttpServletRequest request) {
        ApiError message = new ApiError(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<ApiError>(message, HttpStatus.I_AM_A_TEAPOT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDatesWithSigner.class)
    public ResponseEntity<ApiError> invalidDatesWithSigner(InvalidDatesWithSigner ex, HttpServletRequest request) {
        ApiError message = new ApiError(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<ApiError>(message, HttpStatus.I_AM_A_TEAPOT);
    }

}
