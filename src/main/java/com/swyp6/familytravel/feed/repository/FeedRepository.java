package com.swyp6.familytravel.feed.repository;

import com.swyp6.familytravel.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByLikeListContains(Long userId);

    List<Feed> findByUserIdOrderByCreatedDateTimeDesc(Long userId);
}
