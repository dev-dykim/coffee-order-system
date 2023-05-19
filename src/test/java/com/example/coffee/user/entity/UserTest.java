package com.example.coffee.user.entity;

import com.example.coffee.common.exception.IllegalArgumentCustomException;
import com.example.coffee.common.response.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Entity Test - User")
class UserTest {

    private User user;

    @BeforeEach
    void setup() {
        user = User.of("test1");
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "point", 1000L);
    }

    @Test
    @DisplayName("1 포인트 이상 충전시 정상 충전")
    void givenOneMorePoint_whenChargingPoints_thenWorksFine() {
        // Given
        long previousPoint = user.getPoint();

        // When
        user.plusPoint(1L);

        // Then
        assertThat(user.getPoint()).isEqualTo(previousPoint + 1L);
    }

    @Test
    @DisplayName("0 포인트 이하 충전시 예외 발생")
    void givenZeroLessPoint_whenChargingPoints_thenThrowsException() {
        // Given
        long point = -1L;

        // When
        Exception exception = assertThrows(IllegalArgumentCustomException.class, () -> user.plusPoint(point));

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorType.INVALID_POINT.getMessage());
    }

    @Test
    @DisplayName("포인트 잔액 이하 금액 사용시 예외 발생")
    void givenBalanceUnderPoint_whenUsingPoints_thenWorksFine() {
        // Given
        long previousPoint = user.getPoint();
        long usingPoint = previousPoint;

        // When
        user.minusPoint(usingPoint);

        // Then
        assertThat(user.getPoint()).isEqualTo(previousPoint - usingPoint);
    }

    @Test
    @DisplayName("포인트 잔액 초과 금액 사용시 정상 차감")
    void givenBalanceOverPoint_whenUsingPoints_thenThrowsException() {
        // Given
        long usingPoint = user.getPoint() + 1L;

        // When
        Exception exception = assertThrows(IllegalArgumentCustomException.class, () -> user.minusPoint(usingPoint));

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorType.INSUFFICIENT_POINT.getMessage());
    }
}
