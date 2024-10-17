package com.swyp6.familytravel.notification.entity;

import com.swyp6.familytravel.common.entity.BaseEntity;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public Notification(String content, UserEntity user){
        this.content = Objects.requireNonNull(content);
        this.user = Objects.requireNonNull(user);
    }
}
