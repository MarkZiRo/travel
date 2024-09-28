package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.feed.entity.Feed;
import lombok.Getter;

import java.util.Objects;

@Getter
public class FeedPhotoViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String image;

    public FeedPhotoViewResponse(Feed feed){
        Objects.requireNonNull(feed);
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.image = feed.getImageList().isEmpty() ? null : feed.getImageList().get(0);
    }
}
