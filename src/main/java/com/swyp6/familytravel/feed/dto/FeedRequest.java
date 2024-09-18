package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.feed.entity.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class FeedRequest {
    private String content;
    private String place;
    private Long userId;

    public Feed toFeed(List<String> imageList){
        return Feed.builder()
                .content(content)
                .place(place)
                .userId(userId)
                .imageList(imageList)
                .build();
    }
}
