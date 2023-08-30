package com.devcourse.be04daangnmarket.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devcourse.be04daangnmarket.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
