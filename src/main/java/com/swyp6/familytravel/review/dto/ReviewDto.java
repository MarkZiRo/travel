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
    private String title;
    private String content;
    private String nickName;
    private String profileImage;
    private Long travelId;

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .nickName(review.getUser().getNickName())
                .profileImage(review.getUser().getProfileImage())
                .travelId(review.getTravel().getId())
                .build();
    }
}
