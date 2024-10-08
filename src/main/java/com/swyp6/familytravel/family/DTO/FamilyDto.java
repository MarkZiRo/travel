package com.swyp6.familytravel.family.DTO;

import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.user.dto.UserDto;
import com.swyp6.familytravel.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class FamilyDto {

    private Long id;
    private String familyName;
    private List<UserResponseDto> userList;
    private String profileImage;
    private Map<LocalDate, String> anniversary;

    public static FamilyDto fromEntity(Family family)
    {
        return FamilyDto.builder()
                .id(family.getId())
                .familyName(family.getFamilyName())
                .userList(family.getUserList().stream()
                        .map(UserResponseDto::fromEntity)  // UserDto로 변환
                        .collect(Collectors.toList()))
                .profileImage(family.getProfileImage())
                .anniversary(family.getAnniversary())
                .build();
    }
}
