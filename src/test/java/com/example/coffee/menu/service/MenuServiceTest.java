package com.example.coffee.menu.service;

import com.example.coffee.menu.dto.MenuResponseDto;
import com.example.coffee.menu.dto.PopularMenuResponseDto;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("메뉴 조회 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks private MenuService menuService;

    @Mock private MenuRepository menuRepository;


    @Test
    @DisplayName("전체 메뉴 조회하면, 전체 메뉴를 리스트로 반환한다.")
    void givenMenus_whenSearchAllMenus_thenReturnsAllMenuList() {
        // Given
        Menu menu1 = Menu.of("아메리카노", 4500);
        Menu menu2 = Menu.of("카페라떼", 5000);
        Menu menu3 = Menu.of("카푸치노", 5000);
        ReflectionTestUtils.setField(menu1, "id", 1L);
        ReflectionTestUtils.setField(menu2, "id", 2L);
        ReflectionTestUtils.setField(menu3, "id", 3L);
        given(menuRepository.findAll()).willReturn(List.of(menu1, menu2, menu3));

        // When
        List<MenuResponseDto> menuList = menuService.getAllMenus();

        // Then
        assertThat(menuList).hasSize(3);
        assertThat(menuList)
                .extracting("menuId", "menuName", "menuPrice")
                .containsExactlyInAnyOrder(
                        tuple(1L, "아메리카노", 4500),
                        tuple(2L, "카페라떼", 5000),
                        tuple(3L, "카푸치노", 5000)
                );
        then(menuRepository).should().findAll();
    }

    @Test
    @DisplayName("인기 메뉴 조회하면, 인기 메뉴를 리스트로 반환한다.")
    void givenPopularMenus_whenSearchPopularMenus_thenReturnsPopularMenuList() {
        // Given
        PopularMenuResponseDto menu1 = PopularMenuResponseDto.of(1L, "아메리카노", 10);
        PopularMenuResponseDto menu2 = PopularMenuResponseDto.of(2L, "카페라떼", 4);
        PopularMenuResponseDto menu3 = PopularMenuResponseDto.of(21L, "딸기 요거트 스무디", 8);
        given(menuRepository.findWeeklyTopThreeMenus()).willReturn(List.of(menu1, menu3, menu2));

        // When
        List<PopularMenuResponseDto> menuList = menuService.getPopularMenus(LocalTime.now().getHour());

        // Then
        assertThat(menuList).hasSize(3);
        assertThat(menuList)
                .extracting("menuId", "menuName", "orderCount")
                .containsExactly(
                        tuple(1L, "아메리카노", 10),
                        tuple(21L, "딸기 요거트 스무디", 8),
                        tuple(2L, "카페라떼", 4)
                );
        then(menuRepository).should().findWeeklyTopThreeMenus();
    }
}
