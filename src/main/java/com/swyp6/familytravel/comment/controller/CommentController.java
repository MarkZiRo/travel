package com.swyp6.familytravel.comment.controller;

import com.swyp6.familytravel.comment.dto.CommentRequest;
import com.swyp6.familytravel.comment.dto.CommentResponse;
import com.swyp6.familytravel.comment.entity.Comment;
import com.swyp6.familytravel.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{feedId}")
    public CommentResponse createComment(@PathVariable(name = "feedId") Long feedId, @Valid @RequestBody CommentRequest commentRequest){
        return commentService.createComment(feedId, commentRequest);
    }

    @GetMapping("/like/{commentId}")
    public CommentResponse likeComment(@PathVariable(name = "commentId") Long commentId, @RequestParam Long userId){
        return commentService.addLike(commentId, userId);
    }

    @GetMapping("/removeLike/{commentId}")
    public CommentResponse removeLikeComment(@PathVariable(name = "commentId") Long commentId, @RequestParam Long userId){
        return commentService.removeLike(commentId, userId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable(name = "commentId") Long commentId){
        commentService.deleteComment(commentId);
    }
}
