package com.swyp6.familytravel.user.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.user.dto.CreateUserDto;
import com.swyp6.familytravel.user.dto.UpdateUserDto;
import com.swyp6.familytravel.user.dto.UserDto;
import com.swyp6.familytravel.user.dto.UserProfileDto;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade authFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }

    public UserEntity loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("not found")
        );
    }

    public UserDto createUser(CreateUserDto dto) {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return UserDto.fromEntity(userRepository.save(UserEntity.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getName())
                .profileImage(dto.getProfileImage())
                .build()));

    }

    public void withProfile(UserDto dto)
    {
        UserEntity newUser = UserEntity.builder()
                .email(dto.getEmail())
                .profileImage(dto.getProfileImage())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        UserDto.fromEntity(userRepository.save(newUser));
    }

    public UserDto updateUser(UpdateUserDto dto){

        UserEntity userEntity = authFacade.extractUser();
        userEntity.setNickName(dto.getNickname());

        return UserDto.fromEntity(userRepository.save(userEntity));
    }

    public UserProfileDto getMyProfile() {
        UserEntity member = authFacade.extractUser();
        return UserProfileDto.fromEntity(member);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
