package com.swyp6.familytravel.family.DTO;

import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class FamilyDto {

    private Long id;
    private String familyName;
    private List<UserEntity> userList;
    private String profileImage;
    private Map<LocalDate, String> anniversary;

    public static FamilyDto fromEntity(Family family)
    {
        return FamilyDto.builder()
                .id(family.getId())
                .familyName(family.getFamilyName())
                .userList(family.getUserList())
                .profileImage(family.getProfileImage())
                .anniversary(family.getAnniversary())
                .build();
    }
}
