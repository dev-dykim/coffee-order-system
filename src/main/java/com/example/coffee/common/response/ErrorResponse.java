package com.example.coffee.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private int status;
    private String message;

    @Builder
    private ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse from(ErrorType errorType) {
        return builder()
                .status(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }

    public static ErrorResponse of(String message) {
        return builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }

}

