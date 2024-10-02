package com.swyp6.familytravel.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestDto {

    private String email;
    private String password;
}
