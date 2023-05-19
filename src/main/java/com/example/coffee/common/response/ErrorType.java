package com.example.coffee.common.response;

import lombok.Getter;

@Getter
public enum ErrorType {

    NOT_FOUND_USER(401, "등록된 사용자가 없습니다."),
    INVALID_POINT(400, "충전할 포인트는 1p 이상으로 입력해야 합니다."),
    NOT_FOUND_MENU(400, "메뉴가 존재하지 않습니다."),
    INSUFFICIENT_POINT(400, "포인트 잔액이 부족합니다."),
    FAILED_TO_ACQUIRE_LOCK(400, "락 권한을 얻는데 실패했습니다.")
    ;

    private int code;
    private String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
