package com.swyp6.familytravel.comment.entity;

import com.swyp6.familytravel.common.entity.BaseEntity;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private UserEntity user;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> likeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Comment(String content, UserEntity user, Feed feed) {
        this.content = Objects.requireNonNull(content);
        this.user = Objects.requireNonNull(user);
        this.feed = Objects.requireNonNull(feed);
        feed.addComment(this);
    }

    public void addLike(Long userId) {
        if(userId.equals(this.user.getId())){
            throw new IllegalArgumentException("자신의 댓글에는 좋아요를 누를 수 없습니다.");
        }
        this.likeList.stream().filter(id -> id.equals(userId)).findFirst()
                .ifPresent(id -> {
                    throw new IllegalArgumentException("이미 좋아요를 누른 사용자입니다.");
                });
        this.likeList.add(userId);
    }

    public void removeLike(Long userId) {
        if(userId.equals(this.user.getId())){
            throw new IllegalArgumentException("자신의 댓글에는 좋아요를 누를 수 없습니다.");
        }
        this.likeList.stream().filter(id -> id.equals(userId)).findFirst()
                .orElseThrow(() ->  new IllegalArgumentException("좋아요를 누르지 않은 사용자입니다."));
        this.likeList.remove(userId);
    }

    public int getLikeCnt(){
        return this.likeList.size();
    }

    public void updateComment(String content) {
        this.content = Objects.requireNonNull(content);
    }
}
