package com.swyp6.familytravel.comment.dto;

import com.swyp6.familytravel.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private String comment;
    private Integer likeCnt;
    private LocalDate createdAt;

    public CommentResponse(Comment comment){
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickName();
        this.profileImage = comment.getUser().getProfileImage();
        this.comment = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.createdAt = comment.getCreatedDateTime().toLocalDate();
    }
}
