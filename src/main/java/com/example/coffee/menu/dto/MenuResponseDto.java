package com.example.coffee.menu.dto;

import com.example.coffee.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private Long menuId;
    private String menuName;
    private Integer menuPrice;

    @Builder
    private MenuResponseDto(Long menuId, String menuName, Integer menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static MenuResponseDto from(Menu menu) {
        return builder()
                .menuId(menu.getId())
                .menuName(menu.getMenuName())
                .menuPrice(menu.getMenuPrice())
                .build();
    }
}
