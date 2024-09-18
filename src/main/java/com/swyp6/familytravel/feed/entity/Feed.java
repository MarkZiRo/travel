package com.swyp6.familytravel.feed.entity;

import com.swyp6.familytravel.comment.entity.Comment;
import com.swyp6.familytravel.common.BaseEntity;
import com.swyp6.familytravel.feed.dto.FeedRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
    private Long likeCnt;

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
        this.likeCnt = 0L;
    }

    public void updateFeedContent(FeedRequest feedRequest){
        this.content = Objects.requireNonNullElse(feedRequest.getContent(), content);
        this.place = Objects.requireNonNullElse(feedRequest.getPlace(), place);
    }

    public void addLikeCnt(){
        this.likeCnt ++;
    }

    public void addComment(Comment comment){
        this.commentList.add(comment);
    }

    public void removeComment(Comment comment){
        this.commentList.remove(comment);
    }
}
