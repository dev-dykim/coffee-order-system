package com.example.coffee.common.exception;

import com.example.coffee.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class )
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {

        ErrorResponse errorResponse = ErrorResponse.from(e.getErrorType());
        log.error("NotFoundException throw Exception : {}", e.getErrorType());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = IllegalArgumentCustomException.class )
    public ResponseEntity<ErrorResponse> illegalArgumentCustomException(IllegalArgumentCustomException e) {

        ErrorResponse errorResponse = ErrorResponse.from(e.getErrorType());
        log.error("IllegalArgumentCustomException throw Exception : {}", e.getErrorType());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = RuntimeException.class )
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {

        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        log.error("RuntimeException throw Exception : {}", e.getMessage());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

}

