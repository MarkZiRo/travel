package com.swyp6.familytravel.family.entity;

import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Family {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    @OneToMany(mappedBy = "family")
    private List<UserEntity> userList;
    private String profileImage;
}
