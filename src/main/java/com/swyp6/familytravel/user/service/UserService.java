package com.swyp6.familytravel.user.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.user.dto.CreateUserDto;
import com.swyp6.familytravel.user.dto.UserResponseDto;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.repository.UserRepository;
import com.swyp6.familytravel.user.utils.JpaUserDetailManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(JpaUserDetailManager::fromEntity)
                .orElseThrow(()-> new UsernameNotFoundException("not found"));
    }

    public UserResponseDto createUser(CreateUserDto createUserDto)
    {
        UserEntity user = UserEntity.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .username(createUserDto.getUsername())
                .build();

        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    public void uploadProfileImage(MultipartFile image)
    {
        UserEntity user = authFacade.extractUser();
    }
}
