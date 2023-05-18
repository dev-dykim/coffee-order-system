package com.example.coffee.menu.repository;

import com.example.coffee.menu.dto.PopularMenuResponseDto;
import com.example.coffee.menu.dto.QPopularMenuResponseDto;
import com.example.coffee.menu.entity.QMenu;
import com.example.coffee.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PopularMenuRepositoryImpl implements PopularMenuRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder qOrder = QOrder.order;
    private final QMenu qMenu = QMenu.menu;

    @Override
    public List<PopularMenuResponseDto> findWeeklyTopThreeMenus() {

        LocalDateTime weekAgo = LocalDateTime.of(LocalDate.now().minusDays(6L), LocalTime.of(0, 0));
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .select(new QPopularMenuResponseDto(qOrder.menu.id, qMenu.menuName, qOrder.count().intValue()))
                .from(qOrder)
                .innerJoin(qOrder.menu, qMenu)
                .where(qOrder.createdAt.between(weekAgo, now))
                .groupBy(qOrder.menu.id)
                .orderBy(qOrder.count().desc())
                .limit(3)
                .fetch();
    }
}
