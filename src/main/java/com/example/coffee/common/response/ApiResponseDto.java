package com.example.coffee.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private T data;
    private String message;

    @Builder
    public ApiResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
