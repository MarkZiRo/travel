package com.swyp6.familytravel.feed.controller;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.dto.FeedResponse;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedResponse createFeed(
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles){
        return feedService.createFeed(feedRequest, imageFiles);
    }

    @PatchMapping(path = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedResponse updateFeed(
            @PathVariable("feedId") Long feedId,
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles){
        return feedService.updateFeed(feedId, feedRequest, imageFiles);
    }

    @GetMapping("/{feedId}")
    public FeedResponse getFeed(@PathVariable("feedId") Long feedId){
        return feedService.getFeed(feedId);
    }

    @GetMapping("/{feedId}/like")
    public FeedResponse likeFeed(@PathVariable("feedId") Long feedId, @RequestParam Long userId){
        return feedService.likeFeed(feedId, userId);
    }

    @GetMapping("/{feedId}/dislike")
    public FeedResponse dislikeFeed(@PathVariable("feedId") Long feedId, @RequestParam Long userId){
        return feedService.removeLikeFeed(feedId, userId);
    }

    @GetMapping("/recommend/feedList")
    public PageImpl<Feed> getRecommendFeedList(@RequestParam Long userId, Pageable pageable){
        return feedService.getRecommendFeedList(userId, pageable);
    }

    @GetMapping("/feedList")
    public List<Feed> getUserFeedList(@RequestParam Long userId){
        return feedService.getUserFeedList(userId);
    }

}
