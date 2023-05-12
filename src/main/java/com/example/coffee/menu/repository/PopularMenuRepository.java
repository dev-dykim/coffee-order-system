package com.example.coffee.menu.repository;

import com.example.coffee.menu.dto.PopularMenuResponseDto;

import java.util.List;

public interface PopularMenuRepository {

    List<PopularMenuResponseDto> findWeeklyTopThreeMenus();
}
