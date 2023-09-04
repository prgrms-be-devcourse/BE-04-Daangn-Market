package com.devcourse.be04daangnmarket.comment.repository;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT max(c.commentGroup) FROM Comment c")
    Optional<Integer> findPostCount();
}
