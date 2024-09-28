package com.swyp6.familytravel.auth.entity;

import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String authorities;
    private String profileImage;

    @Getter
    private UserEntity entity;

    public static CustomUserDetails fromEntity(UserEntity entity)
    {
        return CustomUserDetails.builder()
                .entity(entity)
                .build();
    }

    public String getRawAuthorities()
    {
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.stream(authorities.split(","))
                .sorted()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .authorities(authorities)
                .profileImage(profileImage)
                .build();

    }
}
