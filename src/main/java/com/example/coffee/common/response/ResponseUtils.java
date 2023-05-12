package com.example.coffee.common.response;

public class ResponseUtils {

    public static <T> ApiResponseDto<T> ok(T data, MessageType message) {
        return ApiResponseDto.<T>builder()
                .data(data)
                .message(message.getMessage())
                .build();
    }

    public static <T> ApiResponseDto<T> ok(T data) {
        return ApiResponseDto.<T>builder()
                .data(data)
                .build();
    }

    public static ApiResponseDto<Void> ok(MessageType message) {
        return ApiResponseDto.<Void>builder()
                .message(message.getMessage())
                .build();
    }

}

