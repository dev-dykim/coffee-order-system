package com.example.coffee.menu.service;

import com.example.coffee.menu.dto.MenuResponseDto;
import com.example.coffee.menu.dto.PopularMenuResponseDto;
import com.example.coffee.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;


    public List<MenuResponseDto> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(MenuResponseDto::from)
                .toList();
    }

    @Cacheable(cacheNames = "popular-menus")
    public List<PopularMenuResponseDto> getPopularMenus() {
        return menuRepository.findWeeklyTopThreeMenus();
    }

    @Scheduled(cron = "0 0 * * * *")
    @CacheEvict(cacheNames = "popular-menus", allEntries = true)
    public void clearPopularMenusCache() {
    }
}
