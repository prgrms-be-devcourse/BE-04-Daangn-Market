package com.devcourse.be04daangnmarket.post.application;

import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.*;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public PostResponse getPost(Long id) {
		Post post = findPostById(id);

		return toResponse(post);
	}

	public Page<PostResponse> getAllPost(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(this::toResponse);
	}

	@Transactional
	public PostResponse update(Long id, String title, String description, int price, int views,
		TransactionType transactionType, Category category, Status status) {
		Post post = findPostById(id);
		post.update(title, description, price, views, transactionType, category, status);

		return toResponse(post);
	}

	private Post findPostById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_POST.getMessage()));
	}

	@Transactional
	public void delete(Long id) {
		postRepository.deleteById(id);

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
