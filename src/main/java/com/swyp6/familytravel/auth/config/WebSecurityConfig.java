package com.swyp6.familytravel.auth.config;

import com.swyp6.familytravel.auth.filters.JwtTokenFilter;
import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.auth.oauth.OAuth2SuccessHandler;
import com.swyp6.familytravel.auth.oauth.OAuth2UserServiceImpl;
import com.swyp6.familytravel.auth.service.JpaUserDetailManager;
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
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/token/issue","/token/validate","index.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("token/abc").anonymous()
                        .requestMatchers("api/v1/**").authenticated()
                        .anyRequest().denyAll()
        )
//        .oauth2Login(oauth2Login -> oauth2Login
//                .loginPage("/users/login")
//                .successHandler(oAuth2SuccessHandler)
//                .userInfoEndpoint(userInfo -> userInfo
//                .userService(oAuth2UserService))
//        )
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
