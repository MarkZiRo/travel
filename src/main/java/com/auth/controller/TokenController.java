package com.auth.controller;


import com.auth.jwt.JwtRequestDto;
import com.auth.jwt.JwtResponseDto;
import com.auth.jwt.JwtTokenUtils;
import io.jsonwebtoken.Claims;
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
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/issue")
    public JwtResponseDto issueJwt(@RequestBody JwtRequestDto dto)
    {
        if(!manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());

        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

//        if(userDetails.getPassword()
//                .equals(passwordEncoder.encode(dto.getPassword())))

        String jwt = jwtTokenUtils.generateToken(userDetails);

        JwtResponseDto responseDto = new JwtResponseDto();
        responseDto.setToken(jwt);

        return responseDto;
    }


    @GetMapping("/validate")
    public Claims validateToken(@RequestParam("token") String token)
    {
        if(!jwtTokenUtils.validate(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return jwtTokenUtils.parseClaims(token);
    }
}
