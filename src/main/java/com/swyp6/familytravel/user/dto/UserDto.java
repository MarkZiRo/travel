package com.swyp6.familytravel.user.dto;

import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private String password;

    public static UserDto fromEntity(UserEntity userEntity){

        return UserDto.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .profileImage(userEntity.getProfileImage())
                .build();
    }
}
