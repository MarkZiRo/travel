package com.swyp6.familytravel.comment.repository;

import com.swyp6.familytravel.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
