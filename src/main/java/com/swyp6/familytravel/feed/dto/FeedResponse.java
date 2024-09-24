package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.comment.dto.CommentResponse;
import com.swyp6.familytravel.feed.entity.Feed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class FeedResponse {
    private Long id;
    private String title;
    private String content;
    private String place;
    private Long userId;
    private Integer likeCnt;
    private LocalDate createDate;
    private List<String> imageList;
    private List<CommentResponse> commentList = new ArrayList<>();

    @Builder
    public FeedResponse(Feed feed){
        Objects.requireNonNull(feed);
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.place = feed.getPlace();
        this.userId = feed.getUserId();
        this.likeCnt = feed.getLikeCnt();
        this.createDate = LocalDate.from(feed.getCreatedDateTime());
        this.imageList = feed.getImageList();
        this.commentList = feed.getCommentList().stream().map(CommentResponse::new).toList();
    }

}
