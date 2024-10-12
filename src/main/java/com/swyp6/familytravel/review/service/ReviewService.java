package com.swyp6.familytravel.review.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.review.dto.CreateReviewDto;
import com.swyp6.familytravel.review.dto.ReviewDto;
import com.swyp6.familytravel.review.dto.UpdateReviewDto;
import com.swyp6.familytravel.review.entity.Review;
import com.swyp6.familytravel.review.repository.ReviewRepository;
import com.swyp6.familytravel.travel.entity.Travel;
import com.swyp6.familytravel.travel.repository.TravelRepository;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TravelRepository travelRepository;
    private final AuthenticationFacade facade;

    public ReviewDto createReview(Long travelId, CreateReviewDto dto) {
        UserEntity user = facade.extractUser();
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found"));

        Review review = Review.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .travel(travel)
                .build();

        review = reviewRepository.save(review);
        return ReviewDto.fromEntity(review);
    }

    public ReviewDto getReview(Long travelId, Long reviewId) {
        Review review = getReviewEntity(travelId, reviewId);
        return ReviewDto.fromEntity(review);
    }

    public List<ReviewDto> getAllReviewsForTravel(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found"));

        return travel.getReviews().stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ReviewDto updateReview(Long travelId, Long reviewId, UpdateReviewDto dto) {
        Review review = getReviewEntity(travelId, reviewId);

        review.setTitle(dto.getTitle());
        review.setContent(dto.getContent());

        review = reviewRepository.save(review);
        return ReviewDto.fromEntity(review);
    }

    public void deleteReview(Long travelId, Long reviewId) {
        Review review = getReviewEntity(travelId, reviewId);
        reviewRepository.delete(review);
    }

    private Review getReviewEntity(Long travelId, Long reviewId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getTravel().getId().equals(travelId)) {
            throw new IllegalArgumentException("Review does not belong to the specified travel");
        }

        return review;
    }
}
