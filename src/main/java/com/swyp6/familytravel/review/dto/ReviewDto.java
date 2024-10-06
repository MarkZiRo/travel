package com.swyp6.familytravel.review.dto;

import com.swyp6.familytravel.review.entity.Review;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String content;
    private Long travelId;

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .name(review.getName())
                .startDate(review.getStartDate())
                .endDate(review.getEndDate())
                .title(review.getTitle())
                .content(review.getContent())
                .travelId(review.getTravel().getId())
                .build();
    }
}
