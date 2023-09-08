package com.devcourse.be04daangnmarket.post.api;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.dto.PostDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/posts")
public class PostRestController {

	private final int PAGE_SIZE = 10;
	private final PostService postService;

	public PostRestController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping
	public ResponseEntity<PostDto.Response> createPost(
		@Valid PostDto.CreateRequest request,
		@AuthenticationPrincipal User user
	) throws IOException {

		PostDto.Response response = postService.create(
			user.getId(),
			request.title(),
			request.description(),
			request.price(),
			request.transactionType(),
			request.category(),
			request.files()
		);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto.Response> getPost(
		@PathVariable @NotNull Long id,
		HttpServletRequest req,
		HttpServletResponse res
	) {
		PostDto.Response response = postService.getPost(id, req, res);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Page<PostDto.Response>> getAllPost(
		@RequestParam(defaultValue = "0") int page
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<PostDto.Response> responses = postService.getAllPost(pageable);

		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	@GetMapping("/category")
	public ResponseEntity<Page<PostDto.Response>> getPostByCategory(
		@RequestParam @NotNull Category category,
		@RequestParam(defaultValue = "0") int page
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<PostDto.Response> response = postService.getPostByCategory(category, pageable);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<Page<PostDto.Response>> getPostByCategory(
		@PathVariable @NotNull Long memberId,
		@RequestParam(defaultValue = "0") int page
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<PostDto.Response> response = postService.getPostByMemberId(memberId, pageable);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<PostDto.Response>> getPostByKeyword(
		@RequestParam @NotBlank String keyword,
		@RequestParam(defaultValue = "0") int page
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<PostDto.Response> response = postService.getPostByKeyword(keyword, pageable);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostDto.Response> updatePost(
		@PathVariable @NotNull Long id,
		@Valid PostDto.UpdateRequest request
	) {
		PostDto.Response response = postService.update(
			id,
			request.title(),
			request.description(),
			request.price(),
			request.transactionType(),
			request.category(),
			request.files()
		);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<PostDto.Response> updatePostStatus(
		@PathVariable @NotNull Long id,
		@Valid PostDto.StatusUpdateRequest request
	) {
		PostDto.Response response = postService.updateStatus(id, request.status());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		postService.delete(id);

		return ResponseEntity.noContent().build();
	}

}
