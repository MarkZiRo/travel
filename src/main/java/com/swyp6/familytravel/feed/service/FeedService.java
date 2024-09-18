package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageStoreService;
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

    private final ImageStoreService imageStoreService;

    public Feed createFeed(FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles){
        List<String> imageFileNames = imageStoreService.storeImageFiles(imageFiles);
        Feed newFeed = feedRequest.toFeed(imageFileNames);
        return feedRepository.save(newFeed);
    }

    public Feed updateFeed(Long id, FeedRequest feedRequest, Optional<List<MultipartFile>> imageFiles) {
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new RuntimeException("Feed 가 없습니다."));
        feed.updateFeedContent(feedRequest);
        return null;
    }
}
