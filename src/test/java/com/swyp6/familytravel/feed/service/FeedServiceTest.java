package com.swyp6.familytravel.feed.service;

import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private ImageService imageService;

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