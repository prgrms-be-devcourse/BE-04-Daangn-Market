package com.devcourse.be04daangnmarket.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;

@DataJpaTest
class PostRepositoryTest {
	@Autowired
	private PostRepository postRepository;

	@Test
	@DisplayName("검색어를 포함하는 제목을 가진 게시글 전체 조회 성공")
	void findByContentContaining() {
		// given
		List<Post> posts = List.of(
			new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
				Category.DIGITAL_DEVICES),
			new Post(1L, "mouse~!", "this keyboard is good", 100000, TransactionType.SALE,
				Category.DIGITAL_DEVICES),
			new Post(1L, "keyKey~!", "this keyboard is good", 100000, TransactionType.SALE,
				Category.DIGITAL_DEVICES),
			new Post(1L, "house~!", "this keyboard is good", 100000, TransactionType.SALE,
				Category.DIGITAL_DEVICES)
		);
		postRepository.saveAll(posts);

		// when
		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> selectedPost = postRepository.findByTitleContaining("key", pageable);

		// then
		assertEquals(2, selectedPost.getTotalPages());
	}
}
