package com.swyp6.familytravel.user.dto;

import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private String nickName;
    private Long familyId;

    public static UserResponseDto fromEntity(UserEntity userEntity){
        return new UserResponseDto(userEntity);
    }

    public UserResponseDto(UserEntity userEntity){
        Objects.requireNonNull(userEntity);
        this.id = Objects.requireNonNull(userEntity.getId());
        this.username = Objects.requireNonNull(userEntity.getUsername());
        this.email = Objects.requireNonNull(userEntity.getEmail());
        this.nickName = Objects.requireNonNull(userEntity.getNickName());
        this.profileImage = Objects.requireNonNull(userEntity.getProfileImage());
        this.familyId = userEntity.getFamily() == null ? null :  Objects.requireNonNull(userEntity.getFamily().getId());
    }
}
