package com.swyp6.familytravel.notification.repository;

import com.swyp6.familytravel.notification.entity.Notification;
import com.swyp6.familytravel.user.entity.UserEntity;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserOrderByCreatedDateTimeDesc(UserEntity user);
    void deleteByUser(UserEntity user);
}
