package com.swyp6.familytravel.feed.entity;

import com.swyp6.familytravel.comment.entity.Comment;
import com.swyp6.familytravel.common.entity.BaseEntity;
import com.swyp6.familytravel.feed.dto.FeedRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Feed extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String place;
    private Long userId;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> likeList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageList;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Feed(String content, String place, Long userId, List<String> imageList){
        this.content = Objects.requireNonNull(content);
        this.place = Objects.requireNonNull(place);
        this.userId = Objects.requireNonNull(userId);
        this.imageList = Objects.requireNonNull(imageList);
    }

    public void updateFeedContent(FeedRequest feedRequest, List<String> imageList){
        Objects.requireNonNull(feedRequest);
        this.content = Objects.requireNonNullElse(feedRequest.getContent(), content);
        this.place = Objects.requireNonNullElse(feedRequest.getPlace(), place);
        this.imageList = Objects.requireNonNullElse(imageList, this.imageList);
    }

    public void addLike(Long userId){
        this.likeList.stream().filter(id -> id.equals(userId)).findFirst()
                .ifPresent(id -> {
                    throw new IllegalArgumentException("이미 좋아요를 누른 사용자입니다.");
                });
        this.likeList.add(userId);
    }

    public void removeLike(Long userId){
        this.likeList.stream().filter(id -> id.equals(userId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 사용자입니다."));
        this.likeList.remove(userId);
    }

    public int getLikeCnt(){
        return this.likeList.size();
    }

    public void addComment(Comment comment){
        this.commentList.add(comment);
    }

    public void removeComment(Comment comment){
        this.commentList.remove(comment);
    }
}
