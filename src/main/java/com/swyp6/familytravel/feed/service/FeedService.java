package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.dto.FeedResponse;
import com.swyp6.familytravel.feed.dto.FeedSimilarity;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${feed.page-size}")
    private Long PAGE_SIZE;

    private final FeedRepository feedRepository;

    private final ImageService imageStoreService;

    public FeedResponse createFeed(FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles){
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        Feed newFeed = feedRequest.toFeed(imageFileNames);
        feedRepository.save(newFeed);
        return new FeedResponse(newFeed);
    }

    public FeedResponse updateFeed(Long id, FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles) {
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new RuntimeException("Feed 가 없습니다."));
        imageService.deleteImageList(feed.getImageList());
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        feed.updateFeedContent(feedRequest, imageFileNames);
        return new FeedResponse(feed);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed 가 없습니다."));
        return new FeedResponse(feed);
    }

    public FeedResponse likeFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed 가 없습니다."));
        feed.addLike(userId);
        return new FeedResponse(feed);
    }

    public FeedResponse removeLikeFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed 가 없습니다."));
        feed.removeLike(userId);
        return new FeedResponse(feed);
    }

    public List<Feed> getUserFeedList(Long userId){
        return feedRepository.findByUserIdOrderByCreatedDateTimeDesc(userId);
    }

    public PageImpl<Feed> getRecommendFeedList(Long userId, Pageable pageable){

        List<Feed> recommendedFeeds = recommendFeed(userId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), recommendedFeeds.size());

        if(start > end){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(recommendedFeeds.subList(start, end), pageable, recommendedFeeds.size());
    }

    private List<Feed> recommendFeed(Long userId){
        List<Feed> likedFeeds = feedRepository.findAllByLikeListContains(userId);

        return feedRepository.findAll().stream()
                .filter(feed -> !likedFeeds.contains(feed))
                .map(feed -> new FeedSimilarity(feed, calculateAverageSimilarity(feed, likedFeeds)))
                .sorted(Comparator.comparing(FeedSimilarity::similarity).reversed())
                .map(FeedSimilarity::feed)
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
}
