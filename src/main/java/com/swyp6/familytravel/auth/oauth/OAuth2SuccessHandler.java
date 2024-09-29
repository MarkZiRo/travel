package com.swyp6.familytravel.auth.oauth;


import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.user.dto.UserDto;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtils tokenUtils;
 //   private final UserDetailsManager userDetailsManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // OAuth2UserServiceImpl의 반환값이 할당된다.
        OAuth2User oAuth2User
                = (OAuth2User) authentication.getPrincipal();

        // 넘겨받은 정보를 바탕으로 사용자 정보를 준비
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        String username
                = String.format("{%s}%s", provider, email);
        String providerId = oAuth2User.getAttribute("id").toString();
        String profileImage = oAuth2User.getAttribute("profileImg").toString();
        // 처음으로 이 소셜 로그인으로 로그인을 시도했다.
        if (!userService.existsByEmail(email)) {
            // 새 계정을 만들어야 한다.
            userService.withProfile(UserDto.builder()
                    .email(email)
                    .password(passwordEncoder.encode(providerId))
                    .profileImage(profileImage)
                    .build());
        }


        // 데이터베이스에서 사용자 계정 회수
        UserEntity details
                = userService.loadUserByEmail(email);

        // JWT 생성
        String jwt = tokenUtils.generateToken(details);


        // 어디로 리다이렉트 할지 지정
        String targetUrl = String.format(
                "http://localhost:8080/token/validate?token=%s", jwt
        );

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
