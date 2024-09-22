package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private ImageService imageService;

    @Test
    void createFeed() {
        Feed feed = new Feed("content", "place", 1L, List.of());
        feed.addLike(1L);

        when(feedRepository.findAllByLikeListContains(1L)).thenReturn(List.of());
        when(feedRepository.findAll()).thenReturn(List.of(
                new Feed("content", "place", 1L, List.of()),
                new Feed("content", "place", 2L, List.of()),
                new Feed("test", "place", 3L, List.of()),
                new Feed("test", "place", 4L, List.of()),
                new Feed("test", "place", 1L, List.of()),
                feed));

        PageImpl<Feed> recommendFeedList = feedService.getRecommendFeedList(1L, PageRequest.of(3, 2));
        System.out.println(recommendFeedList.getContent());
    }

    @Test
    @DisplayName("피드 수정시 피드가 없으면 에러를 발생시킨다.")
    void failUpdateFeed(){
        //Given
        //When
        //Then
        assertThatThrownBy(() -> feedService.updateFeed(1L, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Feed 가 없습니다.");
    }
}