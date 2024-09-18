package com.swyp6.familytravel.comment.dto;

import com.swyp6.familytravel.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class CommentResponse {
    private Long userId;
    private String comment;
    private Long likeCnt;
    private LocalDate createdAt;

    public CommentResponse(Comment comment){
        this.userId = comment.getUserId();
        this.comment = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.createdAt = comment.getCreatedDateTime().toLocalDate();
    }
}
