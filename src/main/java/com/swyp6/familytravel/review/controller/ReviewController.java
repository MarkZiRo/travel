package com.swyp6.familytravel.review.controller;

import com.swyp6.familytravel.review.dto.CreateReviewDto;
import com.swyp6.familytravel.review.dto.ReviewDto;
import com.swyp6.familytravel.review.dto.UpdateReviewDto;
import com.swyp6.familytravel.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travels/{travelId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@PathVariable Long travelId, @RequestBody CreateReviewDto dto) {
        return reviewService.createReview(travelId, dto);
    }

    @GetMapping("/{reviewId}")
    public ReviewDto getReview(@PathVariable Long travelId, @PathVariable Long reviewId) {
        return reviewService.getReview(travelId, reviewId);
    }

    @GetMapping
    public List<ReviewDto> getAllReviewsForTravel(@PathVariable Long travelId) {
        return reviewService.getAllReviewsForTravel(travelId);
    }

    @PutMapping("/{reviewId}")
    public ReviewDto updateReview(@PathVariable Long travelId, @PathVariable Long reviewId, @RequestBody UpdateReviewDto dto) {
        return reviewService.updateReview(travelId, reviewId, dto);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long travelId, @PathVariable Long reviewId) {
        reviewService.deleteReview(travelId, reviewId);
    }
}