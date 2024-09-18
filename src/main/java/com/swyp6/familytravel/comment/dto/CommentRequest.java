package com.swyp6.familytravel.comment.dto;

import com.swyp6.familytravel.comment.entity.Comment;
import com.swyp6.familytravel.feed.entity.Feed;
import lombok.Getter;

@Getter
public class CommentRequest {
    private String content;
    private Long userId;

    public Comment toComment(Feed feed){
        return Comment.builder()
                .content(content)
                .userId(userId)
                .feed(feed)
                .build();
    }
}
