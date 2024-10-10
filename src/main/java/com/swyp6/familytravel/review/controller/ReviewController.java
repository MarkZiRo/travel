package com.swyp6.familytravel.review.controller;

import com.swyp6.familytravel.review.dto.CreateReviewDto;
import com.swyp6.familytravel.review.dto.ReviewDto;
import com.swyp6.familytravel.review.dto.UpdateReviewDto;
import com.swyp6.familytravel.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/travels/{travelId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성 API", description = "리뷰를 생성합니다.")
    @PostMapping
    public ReviewDto createReview(@PathVariable Long travelId, @RequestBody CreateReviewDto dto) {
        return reviewService.createReview(travelId, dto);
    }

    @Operation(summary = "리뷰 정보 API", description = "리뷰를 얻어옵니다.")
    @GetMapping("/{reviewId}")
    public ReviewDto getReview(@PathVariable Long travelId, @PathVariable Long reviewId) {
        return reviewService.getReview(travelId, reviewId);
    }

    @Operation(summary = "리뷰 정보 API", description = "리뷰를 모두 가져옵니다.")
    @GetMapping
    public List<ReviewDto> getAllReviewsForTravel(@PathVariable Long travelId) {
        return reviewService.getAllReviewsForTravel(travelId);
    }

    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ReviewDto updateReview(@PathVariable Long travelId, @PathVariable Long reviewId, @RequestBody UpdateReviewDto dto) {
        return reviewService.updateReview(travelId, reviewId, dto);
    }

    @Operation(summary = "리뷰 삭제 API", description = "리뷰를 id로 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long travelId, @PathVariable Long reviewId) {
        reviewService.deleteReview(travelId, reviewId);
    }
}