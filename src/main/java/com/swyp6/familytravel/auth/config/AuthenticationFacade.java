package com.swyp6.familytravel.auth.config;

import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.utils.JpaUserDetailManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuth()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserEntity extractUser() {
        JpaUserDetailManager userDetails
                = (JpaUserDetailManager) getAuth().getPrincipal();
        return userDetails.getEntity();
    }
}
