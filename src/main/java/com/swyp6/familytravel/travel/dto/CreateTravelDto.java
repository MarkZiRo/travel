package com.swyp6.familytravel.travel.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateTravelDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
