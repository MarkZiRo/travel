package com.swyp6.familytravel.family.entity;

import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Family {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "family")
    private List<UserEntity> members;
    private String profileImage;
}
