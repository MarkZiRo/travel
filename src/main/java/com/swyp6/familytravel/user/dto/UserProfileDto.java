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
public class UserProfileDto {

    private String username;
    private String nickname;
    private String email;
    private String profileImage;

    public static UserProfileDto fromEntity(UserEntity userEntity)
    {
        return UserProfileDto.builder()
                .username(userEntity.getUsername())
                .nickname(userEntity.getNickName())
                .email(userEntity.getEmail())
                .profileImage(userEntity.getProfileImage())
                .build();
    }
}
