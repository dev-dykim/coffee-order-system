package com.example.coffee.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopularMenuResponseDto {

    private Long menuId;
    private String menuName;
    private int orderCount;

    @Builder
    @QueryProjection
    public PopularMenuResponseDto(Long menuId, String menuName, int orderCount) {
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
