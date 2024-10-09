package com.swyp6.familytravel.user.controller;

import com.swyp6.familytravel.auth.jwt.JwtRequestDto;
import com.swyp6.familytravel.auth.jwt.JwtResponseDto;
import com.swyp6.familytravel.user.dto.CreateUserDto;
import com.swyp6.familytravel.user.dto.UpdateUserDto;
import com.swyp6.familytravel.user.dto.UserDto;
import com.swyp6.familytravel.user.dto.UserProfileDto;
import com.swyp6.familytravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public UserDto signUp(
            @RequestBody
            CreateUserDto dto
    ) {
        return userService.createUser(dto);
    }

    @PostMapping("update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto updateUser(
          @RequestBody UpdateUserDto dto
    ) {
        return userService.updateUser(dto);
    }

    @GetMapping("/my-profile")
    public UserProfileDto getMyProfile() {
        return userService.getMyProfile();
    }


}
