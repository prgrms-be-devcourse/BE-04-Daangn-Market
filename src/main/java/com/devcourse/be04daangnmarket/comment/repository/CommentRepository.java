package com.devcourse.be04daangnmarket.comment.repository;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
