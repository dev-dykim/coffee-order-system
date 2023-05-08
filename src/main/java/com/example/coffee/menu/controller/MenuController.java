package com.example.coffee.menu.controller;

import com.example.coffee.menu.dto.MenuResponseDto;
import com.example.coffee.menu.dto.PopularMenuResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuController {

    @GetMapping("/all-menus")
    List<MenuResponseDto> getAllMenus() {
        return new ArrayList<>();
    }

    @GetMapping("/popular-menus")
    List<PopularMenuResponseDto> getPopularMenus() {
        return new ArrayList<>();
    }
}
