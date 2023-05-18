package com.example.coffee.order.service;

import com.example.coffee.common.exception.IllegalArgumentCustomException;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.menu.repository.MenuRepository;
import com.example.coffee.order.dto.OrderRequestDto;
import com.example.coffee.order.dto.OrderResponseDto;
import com.example.coffee.order.entity.Order;
import com.example.coffee.order.repository.OrderRepository;
import com.example.coffee.user.entity.PointTransaction;
import com.example.coffee.user.entity.User;
import com.example.coffee.user.repository.TestPointTransactionRepository;
import com.example.coffee.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@DisplayName("주문/결제 통합 테스트")
class OrderServiceIntegrationTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;
    @Autowired MenuRepository menuRepository;
    @Autowired TestPointTransactionRepository testPointTransactionRepository;

    @Test
    @Transactional
    @DisplayName("주문 성공")
    void order_success() {
        // Given
        OrderRequestDto requestDto = new OrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "userName", "user1");
        ReflectionTestUtils.setField(requestDto, "menuId", 1L);

        Optional<User> user = userRepository.findByUserName("user1");
        Long beforePoint = user.get().getPoint();

        Optional<Menu> menu = menuRepository.findById(1L);
        int menuPrice = menu.get().getMenuPrice();

        // When
        OrderResponseDto responseDto = orderService.makeOrder(requestDto);

        // Then
        assertThat(user.get().getPoint()).isEqualTo(beforePoint - menuPrice);

        Order order = orderRepository.findById(responseDto.getOrderId()).orElseThrow();
        assertThat(order.getUser()).isEqualTo(user.get());
        assertThat(order.getMenu()).isEqualTo(menu.get());

        PointTransaction transaction = testPointTransactionRepository.findTopOrderByOrderByCreatedAtDesc().orElseThrow();
        assertThat(transaction.getUser()).isEqualTo(user.get());
        assertThat(transaction.getPoint()).isEqualTo(menu.get().getMenuPrice().longValue());
        assertThat(transaction.getPointBalance()).isEqualTo(user.get().getPoint());
    }

    @Test
    @DisplayName("메뉴 1개 시킬 포인트만 있는데 동시에 2개를 주문한 경우")
    void concurrent_two_orders() throws InterruptedException {
        // Given
        User user = User.of("test1");
        ReflectionTestUtils.setField(user, "point", 5000L);
        userRepository.save(user);

        OrderRequestDto requestDto = new OrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "userName", "test1");
        ReflectionTestUtils.setField(requestDto, "menuId", 1L);

        // When
        int numOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    orderService.makeOrder(requestDto);
                    successCount.getAndIncrement();
                } catch (IllegalArgumentCustomException e) {
                    failCount.getAndIncrement();
                }
                latch.countDown();
            });
        }

        latch.await();

        // Then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("메뉴 1개 시킬 포인트만 있는데 동시에 3개를 주문한 경우")
    void concurrent_three_orders() throws InterruptedException {
        // Given
        User user = User.of("test1");
        ReflectionTestUtils.setField(user, "point", 5000L);
        userRepository.save(user);

        OrderRequestDto requestDto = new OrderRequestDto();
        ReflectionTestUtils.setField(requestDto, "userName", "test1");
        ReflectionTestUtils.setField(requestDto, "menuId", 1L);

        // When
        int numOfThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    orderService.makeOrder(requestDto);
                    successCount.getAndIncrement();
                } catch (IllegalArgumentCustomException e) {
                    failCount.getAndIncrement();
                }
                latch.countDown();
            });
        }

        latch.await();

        // Then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(2);
    }
}
