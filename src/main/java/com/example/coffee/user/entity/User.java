package com.example.coffee.user.entity;

import com.example.coffee.common.Timestamped;
import com.example.coffee.common.exception.IllegalArgumentCustomException;
import com.example.coffee.common.response.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "`USER`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String userName;

    @Column
    private Long point;

    @Builder
    private User(String userName, Long point) {
        this.userName = userName;
        this.point = point;
    }

    public static User of(String userName) {
        return builder()
                .userName(userName)
                .point(0L)
                .build();
    }

    public void plusPoint(Long point) {
        if (point <= 0) {
            throw new IllegalArgumentCustomException(ErrorType.INVALID_POINT);
        }
        this.point += point;
    }

    public void minusPoint(Long point) {
        if (this.point - point < 0) {
            throw new IllegalArgumentCustomException(ErrorType.INSUFFICIENT_POINT);
        }
        this.point -= point;
    }
}
