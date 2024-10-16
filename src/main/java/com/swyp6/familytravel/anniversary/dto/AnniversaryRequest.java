package com.swyp6.familytravel.anniversary.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnniversaryRequest {
    private LocalDate date;
    private String content;
}
