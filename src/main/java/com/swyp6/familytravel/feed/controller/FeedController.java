package com.swyp6.familytravel.feed.controller;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Feed createFeed(
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles){
        return feedService.createFeed(feedRequest, imageFiles);
    }

    @PatchMapping(path = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Feed updateFeed(
            @PathVariable("feedId") Long feedId,
            @RequestPart(value = "request") FeedRequest feedRequest,
            @RequestPart(value = "imageFiles", required = false) Optional<List<MultipartFile>> imageFiles){
        return feedService.updateFeed(feedId, feedRequest, imageFiles);
    }
}
