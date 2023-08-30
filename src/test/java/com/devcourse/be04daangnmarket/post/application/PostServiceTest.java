package com.devcourse.be04daangnmarket.post.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostResponse;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Test
	@DisplayName("게시글 등록 성공")
	void createPostTest() {
		// given
		String title = "keyboard~!";
		String description = "this keyboard is good";
		int price = 100000;
		int views = 1000;
		TransactionType transactionType = TransactionType.SALE;
		Category category = Category.DIGITAL_DEVICES;
		Status status = Status.FOR_SALE;

		Post post = Post.builder()
			.title("keyboard~!")
			.description("this keyboard is good")
			.price(100000)
			.views(1000)
			.transactionType(TransactionType.SALE)
			.category(Category.DIGITAL_DEVICES)
			.status(Status.FOR_SALE)
			.build();

		when(postRepository.save(any(Post.class))).thenReturn(post);

		// when
		PostResponse response = postService.create(title, description, price, views, transactionType, category, status);

		// Then
		assertNotNull(response);
		assertEquals(title, response.title());
		assertEquals(description, response.description());
		assertEquals(price, response.price());
		assertEquals(views, response.views());
		assertEquals(transactionType, response.transactionType());
		assertEquals(category, response.category());
		assertEquals(status, response.status());

		verify(postRepository, times(1)).save(any(Post.class));
	}

}