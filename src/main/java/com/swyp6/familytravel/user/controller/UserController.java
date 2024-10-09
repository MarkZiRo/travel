package com.swyp6.familytravel.user.controller;


import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.user.dto.*;
import com.swyp6.familytravel.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 생성 API", description = "유저를 생성합니다.")
    @PostMapping("signup")
    public UserDto signUp(
            @RequestBody
            CreateUserDto dto
    ) {
        return userService.createUser(dto);
    }

    @Operation(summary = "유저 닉네임 API", description = "유저의 닉네임을 정합니다.")
    @PostMapping("update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto updateUser(
          @RequestBody UpdateUserDto dto
    ) {
        return userService.updateUser(dto);
    }

    @Operation(summary = "유저 프로필 API", description = "유저의 프로필을 가져옵니다.")
    @GetMapping("/get")
    public UserResponseDto getMyProfile() {
        return userService.getMyProfile();
    }

}
