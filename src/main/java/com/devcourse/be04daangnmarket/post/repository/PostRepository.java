package com.devcourse.be04daangnmarket.post.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByCategory(Category category, Pageable pageable);

	List<Post> findByMemberId(Long memberId, Pageable pageable);

}
