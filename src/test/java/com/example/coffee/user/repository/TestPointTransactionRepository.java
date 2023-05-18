package com.example.coffee.user.repository;

import com.example.coffee.user.entity.PointTransaction;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@TestConfiguration
public interface TestPointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    Optional<PointTransaction> findTopOrderByOrderByCreatedAtDesc();
}
