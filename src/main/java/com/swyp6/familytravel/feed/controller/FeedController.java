package com.swyp6.familytravel.feed.controller;

import com.swyp6.familytravel.auth.entity.CustomUserDetails;

import com.swyp6.familytravel.feed.dto.FeedPhotoViewResponse;
import com.swyp6.familytravel.feed.dto.FeedPreviewResponse;
import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.dto.FeedDetailResponse;
import com.swyp6.familytravel.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
@Tag(name = "Feed")
@SecurityRequirement(name = "bearerAuth")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 생성 API", description = "이미지 파일과 피드 내용을 받아서 저장하고 피드를 생성합니다.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedDetailResponse createFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false)
            Optional<List<MultipartFile>> imageFiles) {
        return feedService.createFeed(customUserDetails.getEntity(), feedRequest, imageFiles);
    }

    @Operation(summary = "피드 수정 API", description = "이미지 파일과 피드 내용을 받아서 피드를 수정합니다.", security = @SecurityRequirement(name = "JWT"))
    @PatchMapping(path = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FeedDetailResponse updateFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("feedId") Long feedId,
            @Valid
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles) {
        return feedService.updateFeed(customUserDetails.getEntity().getId(), feedId, feedRequest, imageFiles);
    }

    @Operation(summary = "피드 조회 API", description = "피드를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/{feedId}")
    public FeedDetailResponse getFeed(@PathVariable("feedId") Long feedId){
        return feedService.getFeed(feedId);
    }

    @Operation(summary = "피드 삭제 API", description = "피드를 삭제합니다.")
    @DeleteMapping("/{feedId}")
    public void deleteFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "피드 좋아요 API", description = "피드에 좋아요를 남깁니다. 만약 사용자가 이미 좋아요를 남겼으면 오류를 발생시킵니다.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/{feedId}/like")
    public FeedPreviewResponse likeFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("feedId") Long feedId) {
        return feedService.likeFeed(feedId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "피드 좋아요 삭제 API", description = "피드에 좋아요를 지웁니다. 만약 사용자가 좋아요를 남기지 않았다면 오류를 발생시킵니다.", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/{feedId}/removeLike")
    public FeedPreviewResponse dislikeFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("feedId") Long feedId) {
        return feedService.removeLikeFeed(feedId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "사용자별 피드 추천 API", description = "사용자의 좋아요와 비슷한 피드들을 제공합니다. Pageable을 통해 페이징 처리가 가능합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/recommend/feedList")
    public PageImpl<FeedPreviewResponse> getRecommendFeedList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Pageable pageable) {
        return feedService.getRecommendFeedList(customUserDetails.getEntity().getId(), pageable);
    }

    @Operation(summary = "가족 피드 리스트 API", description = "사용자의 가족이 작성한 피드들을 제공합니다. Pageable을 통해 페이징 처리가 가능합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/family/feedList")
    public PageImpl<FeedPreviewResponse> getFamilyFeedList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Pageable pageable) {
        return feedService.getFeedListFamily(customUserDetails.getEntity().getId(), pageable);
    }


    @Operation(summary = "사용자별 피드 리스트 API", description = "사용자가 작성한 피드 리스트를 제공합니다. Pageable을 통해 페이징 처리가 가능합니다.", security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/feedList")
    public PageImpl<FeedPreviewResponse> getUserFeedList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        return feedService.getUserFeedList(customUserDetails.getEntity().getId(), pageable);
    }

}
