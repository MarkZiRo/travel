package com.swyp6.familytravel.comment.controller;

import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import com.swyp6.familytravel.comment.dto.CommentResponse;
import com.swyp6.familytravel.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성 API", description = "피드에 댓글을 생성합니다.")
    @PostMapping("/{feedId}")
    public CommentResponse createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "feedId") Long feedId,
            @RequestBody @NotBlank String content){
        return commentService.createComment(feedId, customUserDetails.getEntity(), content);
    }

    @Operation(summary = "댓글 좋아요 API", description = "댓글에 좋아요를 남깁니다. 만약 사용자가 이미 좋아요를 남겼으면 오류를 발생시킵니다.")
    @GetMapping("/like/{commentId}")
    public CommentResponse likeComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "commentId") Long commentId){
        return commentService.addLike(commentId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "댓글 좋아요 취소 API", description = "댓글에 남긴 좋아요를 취소합니다. 만약 사용자가 좋아요를 남기지 않았다면 오류를 발생시킵니다.")
    @GetMapping("/removeLike/{commentId}")
    public CommentResponse removeLikeComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "commentId") Long commentId){
        return commentService.removeLike(commentId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "commentId") Long commentId
    ){
        commentService.deleteComment(commentId, customUserDetails.getEntity().getId());
    }

    @Operation(summary = "댓글 수정 API", description = "댓글을 수정합니다.")
    @PatchMapping("/{commentId}")
    public CommentResponse updateComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "commentId") Long commentId, @RequestBody @NotBlank String content){
        return commentService.updateComment(commentId, customUserDetails.getEntity().getId(), content);
    }
}
