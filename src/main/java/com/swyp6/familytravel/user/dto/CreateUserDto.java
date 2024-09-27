package com.swyp6.familytravel.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String email;
    private String password;
    private String username;
}
