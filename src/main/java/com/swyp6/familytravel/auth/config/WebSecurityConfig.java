package com.swyp6.familytravel.auth.config;

import com.swyp6.familytravel.auth.filters.JwtTokenFilter;
import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.auth.oauth.OAuth2SuccessHandler;
import com.swyp6.familytravel.auth.oauth.OAuth2UserServiceImpl;
import com.swyp6.familytravel.user.service.JpaUserDetailManager;
import com.swyp6.familytravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserService manager;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(
                                CustomReqeustMatchers.permitAllMatchers
                        )
                        .permitAll()
                        .anyRequest()
                        .permitAll()
        )
        .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/login")
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint(userInfo -> userInfo
                .userService(oAuth2UserService))
        )
        .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(
          session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(
                new JwtTokenFilter(jwtTokenUtils, manager), AuthorizationFilter.class
        )
        ;
        return http.build();
    }


}
