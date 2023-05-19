package com.example.coffee.user.service;

import com.example.coffee.common.exception.IllegalArgumentCustomException;
import com.example.coffee.menu.repository.MenuRepository;
import com.example.coffee.user.dto.PointRequestDto;
import com.example.coffee.user.dto.PointResponseDto;
import com.example.coffee.user.entity.PointTransaction;
import com.example.coffee.user.entity.TransactionType;
import com.example.coffee.user.entity.User;
import com.example.coffee.user.repository.TestPointTransactionRepository;
import com.example.coffee.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@DisplayName("포인트 충전 통합 테스트")
class UserServiceIntegrationTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired MenuRepository menuRepository;
    @Autowired TestPointTransactionRepository pointTransactionRepository;
    @Autowired RedissonClient redissonClient;

    private PointRequestDto requestDto;
    private long point;

    @BeforeEach
    void setup() {
        requestDto = new PointRequestDto();
        point = 1000L;
        ReflectionTestUtils.setField(requestDto, "userName", "user1");
        ReflectionTestUtils.setField(requestDto, "point", point);
    }

    @AfterEach
    void cleanUp() {
        pointTransactionRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("포인트 충전 성공")
    void chargePoint_success() {
        // Given
        User user = userRepository.findByUserName("user1").orElseThrow();
        long previousPoint = user.getPoint();

        // When
        PointResponseDto responseDto = userService.chargePoint(requestDto);

        // Then
        assertThat(responseDto.getBalance()).isEqualTo(previousPoint + point)
                .isEqualTo(user.getPoint());

        PointTransaction transaction = pointTransactionRepository.findTopByOrderByCreatedAtDesc().orElseThrow();
        assertThat(transaction.getPoint()).isEqualTo(point);
        assertThat(transaction.getType()).isEqualTo(TransactionType.CHARGE);
        assertThat(transaction.getPointBalance()).isEqualTo(previousPoint + point)
                .isEqualTo(user.getPoint());
    }

    @Test
    @DisplayName("동시에 2개의 충전 시도한 경우")
    void concurrent_two_orders() throws InterruptedException {
        // Given
        User user = userRepository.findByUserName("user1").orElseThrow();
        long previousPoint = user.getPoint();

        // When
        int numOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    userService.chargePoint(requestDto);
                    successCount.getAndIncrement();
                    User user1 = userRepository.findByUserName("user1").orElseThrow();
                    log.info("point* : " + user1.getPoint());
                } catch (IllegalArgumentCustomException e) {
                    failCount.getAndIncrement();
                }
                latch.countDown();
            });
        }

        latch.await();
        User afterUser = userRepository.findByUserName("user1").orElseThrow();

        // Then
        assertThat(successCount.get()).isEqualTo(numOfThreads);
        assertThat(failCount.get()).isEqualTo(0);
        assertThat(afterUser.getPoint()).isEqualTo(previousPoint + (point) * numOfThreads);

        List<PointTransaction> transactionList = pointTransactionRepository.findAll();
        assertThat(transactionList).hasSize(2);
    }

    @Test
    @DisplayName("동시에 3개의 충전 시도한 경우")
    void concurrent_three_orders() throws InterruptedException {
        // Given
        User user = userRepository.findByUserName("user1").orElseThrow();
        long previousPoint = user.getPoint();

        // When
        int numOfThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    userService.chargePoint(requestDto);
                    successCount.getAndIncrement();
                    User user1 = userRepository.findByUserName("user1").orElseThrow();
                    log.info("point* : " + user1.getPoint());
                } catch (IllegalArgumentCustomException e) {
                    failCount.getAndIncrement();
                }
                latch.countDown();
            });
        }

        latch.await();
        User afterUser = userRepository.findByUserName("user1").orElseThrow();

        // Then
        assertThat(successCount.get()).isEqualTo(numOfThreads);
        assertThat(failCount.get()).isEqualTo(0);
        assertThat(afterUser.getPoint()).isEqualTo(previousPoint + (point) * numOfThreads);

        List<PointTransaction> transactionList = pointTransactionRepository.findAll();
        assertThat(transactionList).hasSize(3);
    }
}
