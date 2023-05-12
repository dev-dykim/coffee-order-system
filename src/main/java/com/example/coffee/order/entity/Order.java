package com.example.coffee.order.entity;

import com.example.coffee.common.Timestamped;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "`ORDER`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Menu menu;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @Builder
    private Order(User user, Menu menu, String menuName, int menuPrice) {
        this.user = user;
        this.menu = menu;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static Order of(User user, Menu menu) {
        return builder()
                .user(user)
                .menu(menu)
                .menuName(menu.getMenuName())
                .menuPrice(menu.getMenuPrice())
                .build();
    }
}
