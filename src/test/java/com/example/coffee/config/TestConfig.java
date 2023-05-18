package com.example.coffee.config;

import com.example.coffee.menu.repository.PopularMenuRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PopularMenuRepositoryImpl popularMenuRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        return new PopularMenuRepositoryImpl(jpaQueryFactory);
    }
}
