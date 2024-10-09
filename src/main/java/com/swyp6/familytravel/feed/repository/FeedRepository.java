package com.swyp6.familytravel.feed.repository;

import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.entity.FeedScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    List<Feed> findAllByLikeListContains(Long userId);

    List<Feed> findAllByScopeOrderByCreatedDateTimeDesc(FeedScope scope);

    List<Feed> findByUserIdOrderByCreatedDateTimeDesc(Long userId);

}
