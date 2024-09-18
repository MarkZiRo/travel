package com.swyp6.familytravel.feed.repository;

import com.swyp6.familytravel.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
