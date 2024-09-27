package com.swyp6.familytravel.feed.entity;

import com.swyp6.familytravel.feed.dto.FeedRequest;
import com.swyp6.familytravel.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class FeedTest {

    private Feed feed;

    @BeforeEach
    void setUp() {
        this.feed = Feed.builder()
                .user(new UserEntity())
                .title("title")
                .place("place")
                .content("content")
                .imageList(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("피드 생성시 null 값이 들어오면 에러를 발생시킨다.")
    void nullParameterFeedTest(){
        //Given
        //When
        //Then
        assertThatThrownBy(() -> Feed.builder()
                .user(null)
                .title("title")
                .place("place")
                .content("content")
                .imageList(new ArrayList<>())
                .build())
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Feed.builder()
                .user(new UserEntity())
                .title(null)
                .place("place")
                .content("content")
                .imageList(new ArrayList<>())
                .build())
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Feed.builder()
                .user(new UserEntity())
                .title("title")
                .place(null)
                .content("content")
                .imageList(new ArrayList<>())
                .build())
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Feed.builder()
                .user(new UserEntity())
                .title("title")
                .place("place")
                .content(null)
                .imageList(new ArrayList<>())
                .build())
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Feed.builder()
                .user(new UserEntity())
                .title("title")
                .place("place")
                .content("content")
                .imageList(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("피드 업데이트 시 null 값이 들어오면 에러를 발생시킨다.")
    void nullParameterUpdateFeedTest() {
        //Given
        //When
        //Then
        assertThatThrownBy(() -> feed.updateFeedContent(null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("피드 업데이트시 업데이트 변경 값이 null 이면 변경하지 않는다.")
    void updateFeedContentNullTest() throws Exception {
        //Given
        //When
        feed.updateFeedContent(of(null, null), null);
        //Then
        assertThat(feed.getContent()).isEqualTo("content");
        assertThat(feed.getPlace()).isEqualTo("place");
    }

    @Test
    @DisplayName("피드 업데이트시 업데이트 변경 값이 있으면 변경한다.")
    void updateFeedContentTest() throws Exception {
        //Given
        //When
        feed.updateFeedContent(of("update content", "update place"), List.of("image1", "image2"));
        //Then
        assertThat(feed.getContent()).isEqualTo("update content");
        assertThat(feed.getPlace()).isEqualTo("update place");
        assertThat(feed.getImageList()).containsAll(List.of("image1", "image2"));
    }

    @Test
    @DisplayName("좋아요를 누른적 없는 사용자가 좋아요를 누르면, like 수가 1 증가한다.")
    void likeTest() {
        //Given
        //When
        feed.addLike(1L);
        //Then
        assertThat(feed.getLikeCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요를 누른 사용자가 좋아요를 누르면 에러를 발생시킨다.")
    void likeFailTest() {
        //Given
        //When
        feed.addLike(1L);
        //Then
        assertThatThrownBy(() -> feed.addLike(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 좋아요를 누른 사용자입니다.");
        assertThat(feed.getLikeCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요를 누른 사용자가 좋아요 제거를 누르면, like 수가 1 감소한다.")
    void removeLikeTest() {
        //Given
        //When
        feed.addLike(1L);
        feed.removeLike(1L);
        //Then
        assertThat(feed.getLikeCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("좋아요를 누르지 않은 사용자가 좋아요 제거를 누르면 에러를 발생시킨다.")
    void removeLikeFailTest() {
        //Given
        //When
        //Then
        assertThatThrownBy(() -> feed.removeLike(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("좋아요를 누르지 않은 사용자입니다.");
        assertThat(feed.getLikeCnt()).isEqualTo(0);
    }

    public static FeedRequest of(String content, String place) throws Exception {
        FeedRequest feedRequest = new FeedRequest();
        Class<?> clazz = Class.forName("com.swyp6.familytravel.feed.dto.FeedRequest");

        Field Field = clazz.getDeclaredField("content");
        Field.setAccessible(true);
        Field.set(feedRequest, content);
        Field = clazz.getDeclaredField("place");
        Field.setAccessible(true);
        Field.set(feedRequest, place);
        System.out.println(feedRequest);
        return feedRequest;
    }


}