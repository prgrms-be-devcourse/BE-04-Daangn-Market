package com.devcourse.be04daangnmarket.post.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.dto.PostRequest;
import com.devcourse.be04daangnmarket.post.dto.PostResponse;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

	@Autowired
	private PostService postService;
	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
		PostResponse response = postService.create(request.title(), request.description()
		,request.price(), request.views(), request.transactionType(), request.category(), request.status());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
