package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.feed.entity.Feed;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class FeedRequest {
    @NotNull
    private String content;
    @NotEmpty
    private String place;
    @NotNull
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
