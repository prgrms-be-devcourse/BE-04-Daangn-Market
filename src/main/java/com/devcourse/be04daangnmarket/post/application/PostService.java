package com.devcourse.be04daangnmarket.post.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostResponse;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

@Service
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Transactional
	public PostResponse create(String title, String description, int price, int views,
		TransactionType transactionType, Category category, Status status) {

		Post post = Post.builder()
			.title(title)
			.description(description)
			.price(price)
			.views(views)
			.transactionType(transactionType)
			.category(category)
			.status(status)
			.build();

		post = postRepository.save(post);

		return toResponse(post);
	}

	private PostResponse toResponse(Post post) {
		return new PostResponse(
			post.getId(),
			post.getTitle(),
			post.getDescription(),
			post.getPrice(),
			post.getViews(),
			post.getTransactionType(),
			post.getCategory(),
			post.getStatus()
		);
	}

}
