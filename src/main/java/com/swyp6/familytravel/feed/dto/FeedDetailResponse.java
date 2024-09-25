package com.swyp6.familytravel.feed.dto;

import com.swyp6.familytravel.comment.dto.CommentResponse;
import com.swyp6.familytravel.feed.entity.Feed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
public class FeedDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String place;
    private final Long userId;
    private final Integer likeCnt;
    private final LocalDate createDate;
    private final List<String> imageList;
    private final List<CommentResponse> commentList;

    @Builder
    public FeedDetailResponse(Feed feed){
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
