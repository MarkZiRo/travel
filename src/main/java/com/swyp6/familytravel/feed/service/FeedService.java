package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.dto.FeedResponse;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class FeedService {

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
        feed.updateFeedContent(feedRequest);
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
}
