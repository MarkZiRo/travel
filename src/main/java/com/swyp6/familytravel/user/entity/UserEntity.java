package com.swyp6.familytravel.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swyp6.familytravel.family.entity.Family;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String email;

    private String authorities;

    private String profileImage;

    private String nickName;

    @JsonIgnore
    @ManyToOne(f)
    @JoinColumn(name = "family_id")
    private Family family;
}
