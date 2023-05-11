package com.example.coffee.order.entity;

import com.example.coffee.common.Timestamped;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
