package com.devcourse.be04daangnmarket.post.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Status;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

import jakarta.servlet.http.Cookie;

@SpringBootTest
class PostServiceTest {
	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Mock
	private ImageService imageService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Test
	@DisplayName("게시글 등록 성공")
	void createPostTest() throws IOException {
		// given
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);
		when(postRepository.save(any(Post.class))).thenReturn(post);

		List<ImageDto.ImageResponse> imageResponses = List.of(
			new ImageDto.ImageResponse(
				"name",
				"type",
				"type",
				1L,
				DomainName.POST,
				1L
			)
		);
		when(imageService.uploadImages(anyList(), eq(DomainName.POST), eq(null))).thenReturn(imageResponses);

		// when
		List<MultipartFile> receivedImages = List.of(
			new MockMultipartFile(
				"images",
				"test-image.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"test image content".getBytes()
			)
		);

		PostDto.Response response = postService.create(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000,
			TransactionType.SALE,
			Category.DIGITAL_DEVICES,
			receivedImages
		);

		// then
		assertNotNull(response);
		assertEquals(1L, response.memberId());
		assertEquals("keyboard~!", response.title());
		assertEquals("this keyboard is good", response.description());
		assertEquals(100000, response.price());
		assertEquals(TransactionType.SALE.getDescription(), response.transactionType());
		assertEquals(Category.DIGITAL_DEVICES.getDescription(), response.category());
		assertEquals(imageResponses, response.images());

		verify(postRepository, times(1)).save(any(Post.class));
		verify(imageService, times(1)).uploadImages(anyList(), eq(DomainName.POST), eq(null));
	}

	@Test
	@DisplayName("게시글 단일 상세 조회 성공")
	void getPostTest() {
		// given
		Long postId = 1L;

		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// when
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		PostDto.Response response = postService.getPost(postId, req, res);

		// then
		assertEquals(post.getId(), response.id());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	@DisplayName("게시글 처음 조회 시 조회수 1 증가")
	void viewUpdateSuccessTest() {
		// given
		Long postId = 1L;

		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// when
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		PostDto.Response response = postService.getPost(postId, req, res);

		// then
		assertEquals(1, response.views());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	@DisplayName("게시물 재조회 시 조회수 변동 없음")
	void viewUpdateFailTest() {
		// given
		Long postId = 1L;

		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// when
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		req.setCookies(new Cookie(postId.toString(), postId.toString()));
		PostDto.Response response = postService.getPost(postId, req, res);

		// then
		assertEquals(0, response.views());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	@DisplayName("게시글 전체 조회 성공")
	public void testGetAllPost() {
		// given
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		Post post2 = new Post(1L,
			"keyboard~!",
			"this keyboard is good",
			100000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		List<Post> posts = List.of(post, post2);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> page = new PageImpl<>(posts, pageable, posts.size());
		when(postRepository.findAll(pageable)).thenReturn(page);

		// when
		Page<PostDto.Response> response = postService.getAllPost(pageable);

		// then
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getNumber(), response.getNumber());
		verify(postRepository, times(1)).findAll(pageable);
	}

	@Test
	@DisplayName("카테고리 기반 게시글 전체 조회 성공")
	public void getPostByCategoryTest() throws Exception {
		// given
		Category category = Category.DIGITAL_DEVICES;
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000,
			TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		List<Post> posts = List.of(post);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> page = new PageImpl<>(posts);
		when(postRepository.findByCategory(category, pageable)).thenReturn(page);

		// when
		Page<PostDto.Response> response = postService.getPostByCategory(category, pageable);

		// then
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getNumber(), response.getNumber());
		verify(postRepository, times(1)).findByCategory(category, pageable);
	}

	@Test
	@DisplayName("사용자자 아이디 기반 게시물 전체조회 성공")
	void getPostByMemberIdTest() {
		// given
		Long memberId = 1L;
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			100000,
			TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		List<Post> posts = List.of(post);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Post> page = new PageImpl<>(posts);
		when(postRepository.findByMemberId(memberId, pageable)).thenReturn(page);

		// when
		Page<PostDto.Response> response = postService.getPostByMemberId(memberId, pageable);

		// then
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getNumber(), response.getNumber());
		verify(postRepository, times(1)).findByMemberId(memberId, pageable);
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

		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			50000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(id)).thenReturn(Optional.of(post));

		// when
		PostDto.Response response = postService.update(
			id,
			title,
			description,
			price,
			transactionType,
			category,
			new ArrayList<MultipartFile>()
		);

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
	@DisplayName("게시글 상태 수정 성공")
	void updatePostStatusTest() {
		// given
		Long postId = 1L;
		Status updateStatus = Status.SOLD;
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			50000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// when
		PostDto.Response response = postService.updateStatus(postId, updateStatus);

		// then
		assertNotNull(response);
		assertEquals(updateStatus.getDescription(), response.status());
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

	@Test
	@DisplayName("게시글 상품 구매시 게시글 구매자 ID 컬럼 저장")
	void PostServiceTest() {
	    // given
	    Long postId =1L;
		Long buyerId = 1L;
		Post post = new Post(
			1L,
			"keyboard~!",
			"this keyboard is good",
			50000, TransactionType.SALE,
			Category.DIGITAL_DEVICES
		);

		when(postRepository.findById(1L)).thenReturn(Optional.of(post));

	    // when
	    PostDto.Response response = postService.purchaseProduct(postId, buyerId);

		// then
		assertEquals(buyerId, response.buyerId());
		verify(postRepository, times(1)).findById(1L);
	}
}
