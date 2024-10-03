package com.swyp6.familytravel.family.entity;

import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
public class Family {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    @OneToMany(mappedBy = "family")
    private List<UserEntity> userList = new ArrayList<>();

    private String profileImage;

    @ElementCollection(fetch = FetchType.LAZY)
    private Map<LocalDate, String> anniversary = new HashMap<>();

}
