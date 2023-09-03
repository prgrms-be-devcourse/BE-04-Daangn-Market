package com.devcourse.be04daangnmarket.post.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Test
	@DisplayName("게시글 등록 성공")
	void createPostTest() throws IOException {
		// given
		String title = "keyboard~!";
		String description = "this keyboard is good";
		int price = 100000;
		TransactionType transactionType = TransactionType.SALE;
		Category category = Category.DIGITAL_DEVICES;
		//List<Image> images = List.of(new Image("name1", "path1"));

		Post post = new Post("keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
				Category.DIGITAL_DEVICES);

		when(postRepository.save(any(Post.class))).thenReturn(post);

		List<MultipartFile> receivedImages = new ArrayList<>();
		MockMultipartFile imageFile = new MockMultipartFile("images", "test-image.jpg",
			MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
		receivedImages.add(imageFile);

		// when
		PostDto.Response response = postService.create(title, description, price, transactionType, category,
			receivedImages);

		// then
		assertNotNull(response);
		assertEquals(title, response.title());
		assertEquals(description, response.description());
		assertEquals(price, response.price());
		assertEquals(transactionType.getDescription(), response.transactionType());
		assertEquals(category.getDescription(), response.category());

		verify(postRepository, times(1)).save(any(Post.class));
	}

	@Test
	@DisplayName("게시글 단일 상세 조회 성공")
	void getPostTest() {
		// given
		Long postId = 1L;

		//List<Image> images = List.of(new Image("name1", "path1"));

		Post post = new Post("keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// when
		PostDto.Response response = postService.getPost(postId);

		// then
		assertNotNull(response);
		assertEquals(post.getId(), response.id());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	@DisplayName("게시글 전체 조회 성공")
	public void testGetAllPost() {
		// given
		//List<Image> images = List.of(new Image("name1", "path1"));

		Post post = new Post("keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		Post post2 = new Post("keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		List<Post> posts = List.of(post, post2);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> page = new PageImpl<>(posts, pageable, posts.size());

		when(postRepository.findAll(pageable)).thenReturn(page);

		// when
		Page<PostDto.Response> responses = postService.getAllPost(pageable);

		// then
		assertEquals(page.getTotalElements(), responses.getTotalElements());
		assertEquals(page.getNumber(), responses.getNumber());

	}

	@Test
	@DisplayName("카테고리 기반 게시글 전체 조회 성공")
	public void getPostByCategoryTest() throws Exception {
		// given
		Category category = Category.DIGITAL_DEVICES;
		List<Image> images = List.of(new Image("name1", "path1"));
		Post post = new Post("keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES, images);

		List<Post> posts = List.of(post);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> page = new PageImpl<>(posts);

		when(postRepository.findByCategory(category, pageable)).thenReturn(page);

		// when
		Page<PostDto.Response> responses = postService.getPostByCategory(category, pageable);

		// then
		assertNotNull(responses);
		assertEquals(page.getTotalElements(), responses.getTotalElements());
		assertEquals(page.getNumber(), responses.getNumber());
		verify(postRepository, times(1)).findByCategory(category, pageable);
	}

	@Test
	@DisplayName("게시글 수정 성공")
	void updatePostTest() {
		// given
		Long id = 1L;
		String title = "keyboard~!";
		String description = "this keyboard is good";
		int price = 100000;
		TransactionType transactionType = TransactionType.SALE;
		Category category = Category.DIGITAL_DEVICES;
		//List<Image> images = List.of(new Image("name1", "path1"));

		Post post = new Post("keyboard~!", "this keyboard is good", 50000, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		when(postRepository.findById(id)).thenReturn(Optional.of(post));

		// when
		PostDto.Response response = postService.update(id, title, description, price,
			transactionType, category, null);

		// then
		assertNotNull(response);
		assertEquals(title, post.getTitle());
		assertEquals(description, post.getDescription());
		assertEquals(price, post.getPrice());
		assertEquals(transactionType, post.getTransactionType());
		assertEquals(category, post.getCategory());

		verify(postRepository, times(1)).findById(id);
	}

	@Test
	@DisplayName("게시글 삭제 성공")
	public void deletePostTest() {
		// given
		Long postId = 1L;

		// when
		postService.delete(postId);

		// then
		verify(postRepository, times(1)).deleteById(postId);
	}

}
