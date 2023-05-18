package com.example.coffee.user.repository;

import com.example.coffee.config.TestConfig;
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
@DisplayName("User Repository Test")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;


    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<User> userList = userRepository.findAll();

        // Then
        assertThat(userList)
                .isNotNull()
                .hasSize(5);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = userRepository.count();
        User user = User.of("testUser1");

        // When
        userRepository.save(user);

        // Then
        assertThat(userRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        long previousCount = userRepository.count();

        // When
        userRepository.delete(user);

        // Then
        assertThat(userRepository.count()).isEqualTo(previousCount - 1);
    }

    @Test
    @DisplayName("update 테스트")
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        User user = userRepository.findById(1L).orElseThrow();
        long previousPoint = user.getPoint();
        user.plusPoint(1000L);

        // When
        User savedUser = userRepository.saveAndFlush(user);

        // Then
        assertThat(savedUser.getPoint()).isEqualTo(previousPoint + 1000L);
    }

}
