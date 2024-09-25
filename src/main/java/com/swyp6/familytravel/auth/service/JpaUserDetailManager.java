package com.swyp6.familytravel.auth.service;

import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.auth.entity.UserEntity;
import com.swyp6.familytravel.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
//@RequiredArgsConstructor
@Service
public class JpaUserDetailManager implements UserDetailsManager {

    private final UserRepository userRepository;

     public JpaUserDetailManager(UserRepository userRepository, PasswordEncoder passwordEncoder)
     {
         this.userRepository = userRepository;
         createUser(CustomUserDetails.builder()
                 .username("user1")
                 .password(passwordEncoder.encode("password1"))
                 .email("user@gmail.com")
                 .authorities("ROLE_USER")
                 .build());
     }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();

        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    @Override
    public void createUser(UserDetails user) {
        if(userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try{
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .email(userDetails.getEmail())
                    .authorities(userDetails.getRawAuthorities())
                    .build();
            userRepository.save(newUser);
        } catch (ClassCastException e)
        {
            log.error("failed : {} ", CustomUserDetails.class);
        }
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);

    }
}
