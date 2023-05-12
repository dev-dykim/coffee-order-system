package com.example.coffee.common.response;

import lombok.Getter;

@Getter
public enum ErrorType {

    NOT_FOUND_USER(401, "등록된 사용자가 없습니다."),
    INVALID_POINT(400, "충전할 포인트는 1p 이상으로 입력해야 합니다.")
    ;

    private int code;
    private String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
