package com.swyp6.familytravel.review.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateReviewDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String content;
}
