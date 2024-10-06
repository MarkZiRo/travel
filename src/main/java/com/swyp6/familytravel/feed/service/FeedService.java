package com.swyp6.familytravel.feed.service;


import com.swyp6.familytravel.feed.dto.*;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.entity.FeedScope;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
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

    public PageImpl<FeedPreviewResponse> getUserFeedList(Long userId, Pageable pageable){
        List<FeedPreviewResponse> userFeedList = feedRepository.findByUserIdOrderByCreatedDateTimeDesc(userId)
                .stream().map(FeedPreviewResponse::new).toList();
        return feedListToPageable(pageable, userFeedList);

    }

    public PageImpl<FeedPreviewResponse> getRecommendFeedList(Long userId, Pageable pageable){

        List<FeedPreviewResponse> recommendedFeeds = recommendFeed(userId);
        return feedListToPageable(pageable, recommendedFeeds);
    }

    private List<FeedPreviewResponse> recommendFeed(Long userId){
        List<Feed> likedFeeds = feedRepository.findAllByLikeListContains(userId);
        log.info("{}", feedRepository.findAllByScopeOrderByCreatedDateTimeDesc(FeedScope.PUBLIC));
        List<FeedPreviewResponse> feedPreviewResponseList = new ArrayList<>(feedRepository.findAllByScopeOrderByCreatedDateTimeDesc(FeedScope.PUBLIC).stream()
                .filter(feed -> !likedFeeds.contains(feed))
                .map(feed -> new FeedSimilarity(feed, calculateAverageSimilarity(feed, likedFeeds)))
                .sorted(Comparator.comparing(FeedSimilarity::similarity).reversed())
                .map(FeedSimilarity::feed)
                .map(FeedPreviewResponse::new)
                .toList());
        List<FeedPreviewResponse> likedFeedPreviewResponse = likedFeeds.stream().map(FeedPreviewResponse::new).toList();
        feedPreviewResponseList.addAll(likedFeedPreviewResponse);
        return feedPreviewResponseList;
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

    public void deleteFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        assert(feed.getUser().getId().equals(userId));
        feedRepository.delete(feed);
    }

    // TODO 가족 생성이 완료되면 가족 피드를 가져올 수 있도록 수정
    public PageImpl<FeedPreviewResponse> getFeedListFamily(UserEntity user, Pageable pageable) {
        List<Long> familyUserList = user.getFamily().getUserList().stream().map(UserEntity::getId).toList();
        List<FeedPreviewResponse> familyFeeds = feedRepository.searchFeedInFamily(familyUserList).stream().map(FeedPreviewResponse::new).toList();
        return feedListToPageable(pageable, familyFeeds);
    }

    public PageImpl<FeedPreviewResponse> getFeedListFamily(Long userId, Pageable pageable) {
        List<FeedPreviewResponse> familyFeeds = feedRepository.searchFeedInFamily(List.of(userId)).stream().map(FeedPreviewResponse::new).toList();
        return feedListToPageable(pageable, familyFeeds);
    }

    private static PageImpl<FeedPreviewResponse> feedListToPageable(Pageable pageable, List<FeedPreviewResponse> feedList) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), feedList.size());

        if(start > end){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(feedList.subList(start, end), pageable, feedList.size());
    }
}
