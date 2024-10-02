package com.swyp6.familytravel.auth.config;

import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFacade {

    private final UserRepository userRepository;
    public Authentication getAuth()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserEntity extractUser() {
        CustomUserDetails userDetails
                = (CustomUserDetails) getAuth().getPrincipal();
        return userDetails.getEntity();
    }

}
