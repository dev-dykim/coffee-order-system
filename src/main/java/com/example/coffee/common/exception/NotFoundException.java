package com.example.coffee.common.exception;

import com.example.coffee.common.response.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends IllegalArgumentException {

    private final ErrorType errorType;

    @Override
    public String getMessage() {
        return errorType.getMessage();
    }
}
