package com.swyp6.familytravel.user.dto;

import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;

    public static UserResponseDto fromEntity(UserEntity userEntity){

        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .profileImage(userEntity.getProfileImage())
                .build();
    }
}
