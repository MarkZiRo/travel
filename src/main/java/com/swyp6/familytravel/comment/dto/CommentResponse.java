package com.swyp6.familytravel.comment.dto;

import com.swyp6.familytravel.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class CommentResponse {
    private Long id;
    private String username;
    private String profileImage;
    private String comment;
    private Integer likeCnt;
    private LocalDate createdAt;

    public CommentResponse(Comment comment){
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.profileImage = comment.getUser().getProfileImage();
        this.comment = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.createdAt = comment.getCreatedDateTime().toLocalDate();
    }
}
