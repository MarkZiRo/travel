package com.swyp6.familytravel.feed.controller;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.dto.FeedResponse;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 생성 API", description = "이미지 파일과 피드 내용을 받아서 저장하고 피드를 생성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedResponse createFeed(
            @Valid
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false)
            Optional<List<MultipartFile>> imageFiles){
        return feedService.createFeed(feedRequest, imageFiles);
    }

    @Operation(summary = "피드 수정 API", description = "이미지 파일과 피드 내용을 받아서 피드를 수정합니다.")
    @PatchMapping(path = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedResponse updateFeed(
            @PathVariable("feedId") Long feedId,
            @Valid
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles){
        return feedService.updateFeed(feedId, feedRequest, imageFiles);
    }

    @Operation(summary = "피드 조회 API", description = "피드를 조회합니다.")
    @GetMapping("/{feedId}")
    public FeedResponse getFeed(@PathVariable("feedId") Long feedId){
        return feedService.getFeed(feedId);
    }

    @Operation(summary = "피드 삭제 API", description = "피드를 삭제합니다.")
    @DeleteMapping("/{feedId}")
    public FeedResponse deleteFeed(@PathVariable("feedId") Long feedId, Long userId){
        return feedService.deleteFeed(feedId, userId);
    }

    @Operation(summary = "피드 좋아요 API", description = "피드에 좋아요를 남깁니다. 만약 사용자가 이미 좋아요를 남겼으면 오류를 발생시킵니다.")
    @PostMapping("/{feedId}/like")
    public FeedResponse likeFeed(@PathVariable("feedId") Long feedId, @RequestParam Long userId){
        return feedService.likeFeed(feedId, userId);
    }

    @Operation(summary = "피드 좋아요 삭제 API", description = "피드에 좋아요를 지웁니다. 만약 사용자가 좋아요를 남기지 않았다면 오류를 발생시킵니다.")
    @PostMapping("/{feedId}/removeLike")
    public FeedResponse dislikeFeed(@PathVariable("feedId") Long feedId,@RequestParam Long userId){
        return feedService.removeLikeFeed(feedId, userId);
    }

    @Operation(summary = "사용자별 피드 추천 API", description = "사용자의 좋아요와 비슷한 피드들을 제공합니다. Pageable을 통해 페이징 처리가 가능합니다.")
    @GetMapping("/recommend/feedList")
    public PageImpl<Feed> getRecommendFeedList(@RequestParam Long userId, Pageable pageable){
        return feedService.getRecommendFeedList(userId, pageable);
    }

    @Operation(summary = "사용자별 피드 리스트 API", description = "사용자가 작성한 피드 리스트를 제공합니다.")
    @GetMapping("/feedList")
    public List<Feed> getUserFeedList(@RequestParam Long userId){
        return feedService.getUserFeedList(userId);
    }

}
