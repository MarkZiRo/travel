package com.swyp6.familytravel.auth.controller;


import com.swyp6.familytravel.auth.jwt.JwtRequestDto;
import com.swyp6.familytravel.auth.jwt.JwtResponseDto;
import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Operation(summary = "토큰 생성 API", description = "email(name)과 password를 받아 토큰을 생성합니다.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/issue")
    public JwtResponseDto issueJwt(@RequestBody JwtRequestDto dto)
    {

        UserEntity details
                = userService.loadUserByEmail(dto.getEmail());

        if(!passwordEncoder.matches(dto.getPassword(), details.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        String jwt = jwtTokenUtils.generateToken(details);

        JwtResponseDto responseDto = new JwtResponseDto();
        responseDto.setToken(jwt);

        return responseDto;
    }

    @Operation(summary = "토큰 검증 API", description = "토큰을 검증합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/validate")
    public Claims validateToken(@RequestParam("token") String token)
    {
        if(!jwtTokenUtils.validate(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return jwtTokenUtils.parseClaims(token);
    }

    @GetMapping("/test")
    public void test(@RequestParam("token") String token)
    {

    }
}
