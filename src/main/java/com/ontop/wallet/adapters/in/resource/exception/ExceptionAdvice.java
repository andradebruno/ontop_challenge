package com.ontop.wallet.adapters.in.resource.exception;

import com.ontop.wallet.adapters.in.resource.response.ErrorResponse;
import com.ontop.wallet.adapters.in.resource.response.ValidationError;
import com.ontop.wallet.adapters.out.exception.NotFoundException;
import com.ontop.wallet.application.core.chain.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMsg(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.NOT_FOUND.toString()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceApiException.class)
    public ResponseEntity<ErrorResponse> handleServiceApiException(ServiceApiException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMsg(ex.getMessage())
                .statusCode(ex.getHttpStatus().value())
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getHttpStatus().toString()).build(), ex.getHttpStatus());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleNoBalanceException(InsufficientFundsException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMsg(ex.getMessage())
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.UNPROCESSABLE_ENTITY.toString()).build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMsg(ex.getMessage())
                .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.NOT_IMPLEMENTED.toString()).build(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (ex.getBindingResult().hasErrors()) {
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                validationErrors.add(ValidationError.builder().field(error.getField()).defaultMsg(error.getDefaultMessage()).build());
            });
        }

        return new ResponseEntity<>(ErrorResponse.builder()
                .validationError(validationErrors)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.toString()).build(), HttpStatus.BAD_REQUEST);
    }

}
