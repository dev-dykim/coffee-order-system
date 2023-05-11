package com.example.coffee.menu.entity;

import com.example.coffee.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer menuPrice;

    @Builder
    private Menu(String menuName, Integer menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    static public Menu of(String menuName, Integer menuPrice) {
        return builder()
                .menuName(menuName)
                .menuPrice(menuPrice)
                .build();
    }
}
