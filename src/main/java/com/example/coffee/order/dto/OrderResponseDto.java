package com.example.coffee.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private Long orderId;

    @Builder
    private OrderResponseDto(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderResponseDto of(Long orderId) {
        return builder()
                .orderId(orderId)
                .build();
    }
}
