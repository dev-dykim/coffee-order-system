package com.example.coffee.menu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PopularMenuResponseDto {

    private Long menuId;
    private String menuName;
    private int orderCount;

    @Builder
    private PopularMenuResponseDto(Long menuId, String menuName, int orderCount) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.orderCount = orderCount;
    }

    public static PopularMenuResponseDto of(Long menuId, String menuName, int orderCount) {
        return builder()
                .menuId(menuId)
                .menuName(menuName)
                .orderCount(orderCount)
                .build();
    }
}
