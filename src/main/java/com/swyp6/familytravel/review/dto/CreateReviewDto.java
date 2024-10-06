package com.swyp6.familytravel.review.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
public class CreateReviewDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String content;
}
