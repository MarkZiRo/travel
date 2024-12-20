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

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String authorities;

    private String profileImage;

    private String nickName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public Family getFamily(){
        if(family == null){
            throw new EntityNotFoundException("가족이 존재하지 않습니다.");
        }
        return family;
    }
}
