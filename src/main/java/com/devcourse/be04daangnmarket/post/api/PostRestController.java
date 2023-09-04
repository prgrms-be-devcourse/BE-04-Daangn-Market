package com.devcourse.be04daangnmarket.post.api;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;

@RestController
@RequestMapping("api/v1/posts")
public class PostRestController {

	public PostRestController(PostService postService) {
		this.postService = postService;
	}

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostDto.Response> createPost(PostDto.CreateRequest request) throws IOException {
		PostDto.Response response = postService.create(request.title(), request.description()
			, request.price(), request.transactionType(), request.category(), request.receivedImages());

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto.Response> getPost(@PathVariable Long id) {
		PostDto.Response response = postService.getPost(id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Page<PostDto.Response>> getAllPost(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PostDto.Response> responses = postService.getAllPost(pageable);

		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	@GetMapping("/category")
	public ResponseEntity<Page<PostDto.Response>> getPostByCategory(@RequestParam Category category,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PostDto.Response> response = postService.getPostByCategory(category, pageable);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<PostDto.Response> updatePost(@PathVariable Long id, PostDto.UpdateRequest request) {

		PostDto.Response response = postService.update(id, request.title(), request.description()
			, request.price(), request.transactionType(), request.category(), request.receivedImages());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		postService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
