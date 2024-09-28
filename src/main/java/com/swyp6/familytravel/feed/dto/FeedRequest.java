package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class FeedRequest {
    @NotEmpty
    private String title;
    @NotNull
    private String content;
    @NotEmpty
    private String place;

    public Feed toFeed(UserEntity user, List<String> imageList){
        return Feed.builder()
                .user(user)
                .title(title)
                .content(content)
                .place(place)
                .imageList(imageList)
                .build();
    }
}
