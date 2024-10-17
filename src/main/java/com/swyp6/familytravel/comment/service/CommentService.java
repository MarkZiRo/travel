package com.swyp6.familytravel.comment.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.comment.dto.CommentResponse;
import com.swyp6.familytravel.comment.entity.Comment;
import com.swyp6.familytravel.comment.repository.CommentRepository;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.repository.FeedRepository;
import com.swyp6.familytravel.notification.service.NotificationService;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final AuthenticationFacade authenticationFacade;
    private final NotificationService notificationService;

    public CommentResponse createComment(Long feedId, String content) {
        UserEntity user = authenticationFacade.extractUser();
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException("해당 피드가 존재하지 않습니다."));
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .feed(feed)
                .build();
        commentRepository.save(comment);
        notificationService.sendToUser(feed.getUser(), user.getNickName() + "님이 댓글을 남겼습니다.");
        return new CommentResponse(user.getId(), comment);
    }

    public CommentResponse updateComment(Long commentId, String content) {
        Long userId = authenticationFacade.extractUser().getId();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
        assert(comment.getUser().getId().equals(userId));
        comment.updateComment(content);
        return new CommentResponse(userId, comment);
    }

    public void deleteComment(Long commentId) {
        Long userId = authenticationFacade.extractUser().getId();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
        assert(comment.getUser().getId().equals(userId));
        comment.getFeed().removeComment(comment);
        commentRepository.deleteById(commentId);
    }

    public CommentResponse addLike(Long commentId) {
        UserEntity user = authenticationFacade.extractUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
        comment.addLike(user.getId());
        notificationService.sendToUser(comment.getUser(), user.getNickName() + "님이 댓글에 좋아요를 눌렀습니다.");
        return new CommentResponse(user.getId(), comment);
    }

    public CommentResponse removeLike(Long commentId) {
        UserEntity user = authenticationFacade.extractUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
        comment.removeLike(user.getId());
        notificationService.sendToUser(comment.getUser(), user.getNickName() + "님이 댓글에 좋아요를 제거했습니다.");
        return new CommentResponse(user.getId(), comment);
    }
}
