package com.swift6.familytravel.auth.config;

import com.swift6.familytravel.auth.filters.JwtTokenFilter;
import com.swift6.familytravel.auth.jwt.JwtTokenUtils;
import com.swift6.familytravel.auth.oauth.OAuth2UserServiceImpl;
import com.swift6.familytravel.auth.service.JpaUserDetailManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final JpaUserDetailManager manager;
    private final OAuth2UserServiceImpl oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/token/issue",
                                         "/token/validate").permitAll()
                        .requestMatchers("token/abc")
                        .anonymous()
                        .requestMatchers("/auth/user-role")
                        .hasRole("USER")
                        .anyRequest()
                        .permitAll()
        )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/users/login")
                        .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService))
                )
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
