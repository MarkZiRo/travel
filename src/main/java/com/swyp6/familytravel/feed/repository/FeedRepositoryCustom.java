package com.swyp6.familytravel.feed.repository;

import com.swyp6.familytravel.feed.entity.Feed;

import java.util.List;

public interface FeedRepositoryCustom {
    List<Feed> searchFeedInFamily(List<Long> userId);
}
