package com.devcourse.be04daangnmarket.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findByCategory(Category category, Pageable pageable);

	Page<Post> findByMemberId(Long memberId, Pageable pageable);

	Page<Post> findByTitleContaining(String title, Pageable pageable);

}
