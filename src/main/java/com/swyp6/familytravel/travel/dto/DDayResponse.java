package com.swyp6.familytravel.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DDayResponse {
    private String name;
    private Long dDay;
}
