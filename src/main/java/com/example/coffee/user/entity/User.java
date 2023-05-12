package com.example.coffee.user.entity;

import com.example.coffee.common.Timestamped;
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
        this.point += point;
    }
}
