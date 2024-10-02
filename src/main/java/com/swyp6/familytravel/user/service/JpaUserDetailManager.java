package com.swyp6.familytravel.user.service;

import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JpaUserDetailManager implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String authorities;
    private String profileImage;
    private String nickName;

    @Getter
    private UserEntity entity;

    public static JpaUserDetailManager fromEntity(UserEntity entity) {
        return JpaUserDetailManager.builder()
                .entity(entity)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(entity.getAuthorities().split(","))
            .map(role -> (GrantedAuthority) () -> role)
            .toList();
    }

    @Override
    public String getPassword() {
        return this.entity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.entity.getUsername();
    }

    public Long getId() {
        return this.entity.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
