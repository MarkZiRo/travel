package com.swyp6.familytravel.user.dto;

import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private String nickName;

    public UserResponseDto(UserEntity userEntity){
        Objects.requireNonNull(userEntity);
        this.id = Objects.requireNonNull(userEntity.getId());
        this.username = Objects.requireNonNull(userEntity.getUsername());
        this.email = Objects.requireNonNull(userEntity.getEmail());
        this.nickName = Objects.requireNonNull(userEntity.getNickName());
        this.profileImage = Objects.requireNonNull(userEntity.getProfileImage());
    }
}
