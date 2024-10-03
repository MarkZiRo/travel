package com.swyp6.familytravel.family.DTO;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FamilyAnniversaryDto {
    private LocalDate anniversaryDate;
    private String anniversaryContent;
}
