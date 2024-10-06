package com.swyp6.familytravel.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp6.familytravel.feed.entity.Feed;
import com.swyp6.familytravel.feed.entity.FeedScope;
import com.swyp6.familytravel.feed.entity.QFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Feed> searchFeedInFamily(List<Long> userId) {
        QFeed feed = QFeed.feed;
        return queryFactory.selectFrom(feed)
                .where(feed.user.id.in(userId))
                .orderBy(feed.createdDateTime.desc())
                .fetch();
    }
}
