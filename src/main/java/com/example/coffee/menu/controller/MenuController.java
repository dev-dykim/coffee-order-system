package com.example.coffee.menu.controller;

import com.example.coffee.menu.dto.MenuResponseDto;
import com.example.coffee.menu.dto.PopularMenuResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Menu", description = "menu API document")
public class MenuController {

    @GetMapping("/all-menus")
    @Operation(summary = "커피 메뉴 목록 조회", description = "메뉴 ID, 메뉴 이름, 메뉴 가격을 출력합니다.", tags = {"Menu"})
    List<MenuResponseDto> getAllMenus() {
        return new ArrayList<>();
    }

    @GetMapping("/popular-menus")
    @Operation(summary = "인기 메뉴 목록 조회", description = "최근 7일간 인기있는 메뉴 3개(메뉴 이름, 주문 횟수)를 출력합니다.", tags = {"Menu"})
    List<PopularMenuResponseDto> getPopularMenus() {
        return new ArrayList<>();
    }
}
