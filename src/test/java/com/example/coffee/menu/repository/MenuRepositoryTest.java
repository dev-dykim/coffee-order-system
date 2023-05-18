package com.example.coffee.menu.repository;

import com.example.coffee.config.TestConfig;
import com.example.coffee.menu.dto.PopularMenuResponseDto;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.order.entity.Order;
import com.example.coffee.order.repository.OrderRepository;
import com.example.coffee.user.entity.User;
import com.example.coffee.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@DisplayName("Menu Repository Test")
public class MenuRepositoryTest {

    @Autowired private MenuRepository menuRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private PopularMenuRepositoryImpl popularMenuRepository;


    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Menu> menus = menuRepository.findAll();

        // Then
        assertThat(menus)
                .isNotNull()
                .hasSize(23);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = menuRepository.count();
        Menu menu = Menu.of("새로운 메뉴", 1000);

        // When
        menuRepository.save(menu);

        // Then
        assertThat(menuRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Menu menu = menuRepository.findById(1L).orElseThrow();
        long previousCount = menuRepository.count();

        // When
        menuRepository.delete(menu);

        // Then
        assertThat(menuRepository.count()).isEqualTo(previousCount - 1);
    }

    @Test
    @DisplayName("[Querydsl] 인기 메뉴 3개 조회")
    void givenOrders_whenQueryingPopularMenus_thenReturnPopularResponseDtoList() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        Menu menu1 = menuRepository.findById(1L).orElseThrow();
        Menu menu2 = menuRepository.findById(2L).orElseThrow();
        Menu menu3 = menuRepository.findById(3L).orElseThrow();
        orderRepository.save(Order.of(user, menu1));
        orderRepository.save(Order.of(user, menu2));
        orderRepository.save(Order.of(user, menu2));
        orderRepository.save(Order.of(user, menu3));
        orderRepository.save(Order.of(user, menu3));
        orderRepository.save(Order.of(user, menu3));

        // When
        List<PopularMenuResponseDto> menuList = popularMenuRepository.findWeeklyTopThreeMenus();

        // Then
        assertThat(menuList).hasSize(3);
        assertThat(menuList)
                .extracting("menuId", "menuName", "orderCount")
                .containsExactly(
                        tuple(3L, "카푸치노", 3),
                        tuple(2L, "카페라떼", 2),
                        tuple(1L, "아메리카노", 1)
                );
    }

}
