package com.swyp6.familytravel.comment.entity;

import com.swyp6.familytravel.comment.dto.CommentRequest;
import com.swyp6.familytravel.common.BaseEntity;
import com.swyp6.familytravel.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Long userId;
    private Long likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Comment(String content, Long userId, Feed feed) {
        this.content = Objects.requireNonNull(content);
        this.userId = Objects.requireNonNull(userId);
        this.feed = Objects.requireNonNull(feed);
        this.likeCnt = 0L;
        feed.addComment(this);
    }

    public void addLikeCnt() {
        this.likeCnt++;
    }

    public void updateComment(String content) {
        this.content = Objects.requireNonNull(content);
    }
}
