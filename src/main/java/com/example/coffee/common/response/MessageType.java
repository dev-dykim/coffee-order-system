package com.example.coffee.common.response;

import lombok.Getter;

@Getter
public enum MessageType {

    CHARGE_SUCCESSFULLY("충전이 완료되었습니다.");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }
}
