package com.devcourse.be04daangnmarket.post.application;

import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

@Service
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Transactional
	public PostDto.Response create(String title, String description, int price,
		TransactionType transactionType, Category category) {

		Post post = new Post(title, description, price, transactionType, category);
		post = postRepository.save(post);

		return toResponse(post);
	}

	public PostDto.Response getPost(Long id) {
		Post post = findPostById(id);

		return toResponse(post);
	}

	public Page<PostDto.Response> getAllPost(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(this::toResponse);
	}

	@Transactional
	public PostDto.Response update(Long id, String title, String description, int price,
		TransactionType transactionType, Category category) {
		Post post = findPostById(id);
		post.update(title, description, price, transactionType, category);

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

	private PostDto.Response toResponse(Post post) {
		return new PostDto.Response(
			post.getId(),
			post.getTitle(),
			post.getDescription(),
			post.getPrice(),
			post.getViews(),
			post.getTransactionType().getDescription(),
			post.getCategory().getDescription(),
			post.getStatus().getDescription()
		);
	}

}
