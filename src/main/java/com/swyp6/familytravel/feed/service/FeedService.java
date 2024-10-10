package com.swyp6.familytravel.feed.service;


import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.family.entity.Family;
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

    private final AuthenticationFacade authFacade;


    public FeedDetailResponse createFeed(FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles){
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        UserEntity user = authFacade.extractUser();
        Feed newFeed = feedRequest.toFeed(user, imageFileNames);
        feedRepository.save(newFeed);
        return new FeedDetailResponse(user.getId(), newFeed);
    }

    public FeedDetailResponse updateFeed(Long feedId, FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles) {
        Long userId = authFacade.extractUser().getId();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        assert(feed.getUser().getId().equals(userId));
        imageService.deleteImageList(feed.getImageList());
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        feed.updateFeedContent(feedRequest, imageFileNames);
        return new FeedDetailResponse(userId, feed);
    }

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeed(Long feedId) {
        Long userId = authFacade.extractUser().getId();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        return new FeedDetailResponse(userId, feed);
    }

    public FeedPreviewResponse likeFeed(Long feedId) {
        Long userId = authFacade.extractUser().getId();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        feed.addLike(userId);
        return new FeedPreviewResponse(userId, feed);
    }

    public FeedPreviewResponse removeLikeFeed(Long feedId) {
        Long userId = authFacade.extractUser().getId();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        feed.removeLike(userId);
        return new FeedPreviewResponse(userId, feed);
    }

    @Transactional(readOnly = true)
    public PageImpl<FeedPreviewResponse> getUserFeedList(Pageable pageable){
        Long userId = authFacade.extractUser().getId();
        List<FeedPreviewResponse> userFeedList = feedRepository.findByUserIdOrderByCreatedDateTimeDesc(userId)
                .stream().map((feed -> new FeedPreviewResponse(userId, feed))).toList();
        return feedListToPageable(pageable, userFeedList);

    }

    @Transactional(readOnly = true)
    public PageImpl<FeedPreviewResponse> getRecommendFeedList(Pageable pageable){
        Long userId = authFacade.extractUser().getId();
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
                .map(feed -> new FeedPreviewResponse(userId, feed))
                .toList());
        List<FeedPreviewResponse> likedFeedPreviewResponse = likedFeeds.stream().map(feed -> new FeedPreviewResponse(userId, feed)).toList();
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

    public void deleteFeed(Long feedId) {
        Long userId = authFacade.extractUser().getId();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("Feed 가 없습니다."));
        assert(feed.getUser().getId().equals(userId));
        feedRepository.delete(feed);
    }

    @Transactional(readOnly = true)
    public PageImpl<FeedPreviewResponse> getFeedListFamily(Pageable pageable) {
        UserEntity user = authFacade.extractUser();
        Family family = user.getFamily();
        List<Long> familyUserList = (family == null) ? List.of(user.getId()) : family.getUserList().stream().map(UserEntity::getId).toList();
        List<FeedPreviewResponse> familyFeeds = feedRepository.searchFeedInFamily(familyUserList).stream().map(feed -> new FeedPreviewResponse(user.getId(), feed)).toList();
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
