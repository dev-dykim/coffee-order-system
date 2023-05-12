package com.example.coffee.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PointResponseDto {

    private Long balance;

    @Builder
    private PointResponseDto(Long balance) {
        this.balance = balance;
    }

    public static PointResponseDto of(Long balance) {
        return builder()
                .balance(balance)
                .build();
    }
}
