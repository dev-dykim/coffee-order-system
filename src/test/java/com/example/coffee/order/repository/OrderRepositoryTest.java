package com.example.coffee.order.repository;

import com.example.coffee.config.TestConfig;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.menu.repository.MenuRepository;
import com.example.coffee.order.entity.Order;
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

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@DisplayName("Order Repository Test")
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;
    @Autowired MenuRepository menuRepository;


    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        Menu menu = menuRepository.findById(1L).orElseThrow();
        orderRepository.save(Order.of(user, menu));

        // When
        List<Order> orders = orderRepository.findAll();

        // Then
        assertThat(orders)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = orderRepository.count();
        User user = userRepository.findById(1L).orElseThrow();
        Menu menu = menuRepository.findById(1L).orElseThrow();

        // When
        orderRepository.save(Order.of(user, menu));

        // Then
        assertThat(orderRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        Menu menu = menuRepository.findById(1L).orElseThrow();
        orderRepository.save(Order.of(user, menu));
        Order order = orderRepository.save(Order.of(user, menu));
        long previousCount = orderRepository.count();

        // When
        orderRepository.delete(order);

        // Then
        assertThat(orderRepository.count()).isEqualTo(previousCount - 1);
    }
}