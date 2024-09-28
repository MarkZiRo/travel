package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.dto.*;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class FeedService {

    private final ImageService imageService;

    private final FeedRepository feedRepository;

    private final ImageService imageStoreService;

    public FeedDetailResponse createFeed(UserEntity user, FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles){
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        Feed newFeed = feedRequest.toFeed(user, imageFileNames);
        feedRepository.save(newFeed);
        return new FeedDetailResponse(newFeed);
    }

    public FeedDetailResponse updateFeed(Long userId, Long feedId, FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        assert(feed.getUser().getId().equals(userId));
        imageService.deleteImageList(feed.getImageList());
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        feed.updateFeedContent(feedRequest, imageFileNames);
        return new FeedDetailResponse(feed);
    }

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        return new FeedDetailResponse(feed);
    }

    public FeedPreviewResponse likeFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        feed.addLike(userId);
        return new FeedPreviewResponse(feed);
    }

    public FeedPreviewResponse removeLikeFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        feed.removeLike(userId);
        return new FeedPreviewResponse(feed);
    }

    public List<FeedPhotoViewResponse> getUserFeedList(Long userId){
        return feedRepository.findByUserIdOrderByCreatedDateTimeDesc(userId)
                .stream().map(FeedPhotoViewResponse::new).toList();
    }

    public PageImpl<FeedPreviewResponse> getRecommendFeedList(Long userId, Pageable pageable){

        List<FeedPreviewResponse> recommendedFeeds = recommendFeed(userId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), recommendedFeeds.size());

        if(start > end){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(recommendedFeeds.subList(start, end), pageable, recommendedFeeds.size());
    }

    private List<FeedPreviewResponse> recommendFeed(Long userId){
        List<Feed> likedFeeds = feedRepository.findAllByLikeListContains(userId);

        return feedRepository.findAll().stream()
                .filter(feed -> !likedFeeds.contains(feed))
                .map(feed -> new FeedSimilarity(feed, calculateAverageSimilarity(feed, likedFeeds)))
                .sorted(Comparator.comparing(FeedSimilarity::similarity).reversed())
                .map(FeedSimilarity::feed)
                .map(FeedPreviewResponse::new)
                .toList();
    }

    private double calculateAverageSimilarity(Feed feed, List<Feed> likedFeeds) {
        return likedFeeds.stream()
                .mapToDouble(likedFeed -> calculateSimilarity(feed, likedFeed))
                .average()
                .orElse(0.0);
    }

    private double calculateSimilarity(Feed feed, Feed likedFeed) {
        Set<String> feedWords = new HashSet<>(Arrays.asList(feed.getContent().split("\\s+")));
        Set<String> likedFeedWords = new HashSet<>(Arrays.asList(likedFeed.getContent().split("\\s+")));

        Set<String> intersection = new HashSet<>(feedWords);
        intersection.retainAll(likedFeedWords);

        Set<String> union = new HashSet<>(feedWords);
        union.addAll(likedFeedWords);

        return (double) intersection.size() / union.size();
    }

    public FeedDetailResponse deleteFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        assert(feed.getUser().getId().equals(userId));
        feedRepository.delete(feed);
        return new FeedDetailResponse(feed);
    }
}
