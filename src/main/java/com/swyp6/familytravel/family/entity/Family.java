package com.swyp6.familytravel.family.entity;

import com.swyp6.familytravel.travel.entity.Travel;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Family {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> userList = new ArrayList<>();

    private String profileImage;

    @ElementCollection(fetch = FetchType.LAZY)
    private Map<LocalDate, String> anniversary = new HashMap<>();

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Travel> travels = new ArrayList<>();

    public void addTravel(Travel travel) {
        this.travels.add(travel);
        travel.setFamily(this);
    }

    public void removeTravel(Travel travel) {
        this.travels.remove(travel);
    }

    public void addUser(UserEntity user) {
        userList.add(user);
        user.setFamily(this);  // 양방향 관계 설정
    }
}
