package com.example.coffee.user.entity;

import com.example.coffee.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column
    private Long point;

    @Column
    private Long pointBalance;

    @Builder
    private PointTransaction(User user, TransactionType type, Long point, Long pointBalance) {
        this.user = user;
        this.type = type;
        this.point = point;
        this.pointBalance = pointBalance;
    }

    public static PointTransaction of(User user, TransactionType type, Long point) {
        return builder()
                .user(user)
                .type(type)
                .point(point)
                .pointBalance(user.getPoint())
                .build();
    }
}
