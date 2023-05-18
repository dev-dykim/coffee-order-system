package com.example.coffee.user.repository;

import com.example.coffee.config.TestConfig;
import com.example.coffee.user.entity.PointTransaction;
import com.example.coffee.user.entity.TransactionType;
import com.example.coffee.user.entity.User;
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
@DisplayName("PointTransaction Repository Test")
class PointTransactionRepositoryTest {

    @Autowired PointTransactionRepository pointTransactionRepository;
    @Autowired UserRepository userRepository;


    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.CHARGE, 1000L));
        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.USE, 500L));

        // When
        List<PointTransaction> transactionList = pointTransactionRepository.findAll();

        // Then
        assertThat(transactionList)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        long previousCount = pointTransactionRepository.count();
        PointTransaction transaction = PointTransaction.of(user, TransactionType.CHARGE, 1000L);

        // When
        pointTransactionRepository.save(transaction);

        // Then
        assertThat(pointTransactionRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.CHARGE, 1000L));
        PointTransaction transaction = pointTransactionRepository.save(PointTransaction.of(user, TransactionType.USE, 500L));
        long previousCount = pointTransactionRepository.count();

        // When
        pointTransactionRepository.delete(transaction);

        // Then
        assertThat(pointTransactionRepository.count()).isEqualTo(previousCount - 1);
    }

}
