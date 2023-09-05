package com.devcourse.be04daangnmarket.post.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;

@WebMvcTest(PostRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class PostRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Test
	@DisplayName("게시글 등록 REST API 성공")
	public void createPostTest() throws Exception {

		// given
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"test-file.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"This is a test file content".getBytes()
		);

		PostDto.Response mockResponse = new PostDto.Response(1L, 1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), null);

		when(postService.create(1L, "Keyboard", "nice Keyboard", 100,
			TransactionType.SALE, Category.DIGITAL_DEVICES, null)).thenReturn(mockResponse);

		// when then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
				.file(file)
				.param("title", "Keyboard")
				.param("description", "nice Keyboard")
				.param("price", "100")
				.param("transactionType", TransactionType.SALE.name())
				.param("category", Category.DIGITAL_DEVICES.name()))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(jsonPath("$.title").value("Keyboard"));
	}

	@Test
	@DisplayName("게시글 단일 상세 조회 REST API 성공")
	public void getPostTest() throws Exception {
		// given
		Long postId = 1L;
		PostDto.Response mockResponse = new PostDto.Response(1L, 1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), null);

		when(postService.getPost(1L, null, null)).thenReturn(mockResponse);

		// when then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Keyboard"))
			.andExpect(jsonPath("$.description").value("nice Keyboard"))
			.andExpect(jsonPath("$.price").value(100))
			.andExpect(jsonPath("$.views").value(1000))
			.andExpect(jsonPath("$.transactionType").value(TransactionType.SALE.getDescription()))
			.andExpect(jsonPath("$.category").value(Category.DIGITAL_DEVICES.getDescription()))
			.andExpect(jsonPath("$.status").value(Status.FOR_SALE.getDescription()));

	}

	@Test
	@DisplayName("게시글 전체 조회 REST API 성공")
	public void getAllPostTest() throws Exception {
		// given
		PostDto.Response postResponse1 = new PostDto.Response(1L, 1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), null);

		PostDto.Response postResponse2 = new PostDto.Response(1L, 1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), null);

		List<PostDto.Response> fakeResponses = List.of(postResponse1, postResponse2);

		Pageable pageable = PageRequest.of(0, 10);
		Page<PostDto.Response> responsePage = new PageImpl<>(fakeResponses, pageable, fakeResponses.size());

		// when
		when(postService.getAllPost(pageable)).thenReturn(responsePage);

		// then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(fakeResponses.size()));

	}

	@Test
	@DisplayName("게시글 카테고리 기반 전체 조회 REST API 성공")
	public void GetPostByCategoryTest() throws Exception {
		// given
		Category category = Category.DIGITAL_DEVICES;
		PageRequest pageable = PageRequest.of(0, 10);

		PostDto.Response postResponse = new PostDto.Response(1L, 1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), null);

		List<PostDto.Response> mockResponses = List.of(postResponse);

		when(postService.getPostByCategory(category, pageable)).thenReturn(new PageImpl<>(mockResponses));

		// when then
		mockMvc.perform(get("/api/v1/posts/category")
				.param("category", category.toString())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title").value("Keyboard"))
			.andExpect(jsonPath("$.content[0].description").value("nice Keyboard"));

	}

	@Test
	@DisplayName("게시글 상태 변경 REST API 성공")
	void updatePostStatusTest() throws Exception {
		// given
		Long postId = 1L;
		PostDto.StatusUpdateRequest mockRequest = new PostDto.StatusUpdateRequest(Status.SOLD);

		PostDto.Response mockResponse = new PostDto.Response(
			1L,
			1L,
			"Keyboard",
			"nice Keyboard",
			100,
			1000,
			TransactionType.SALE.getDescription(),
			Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(),
			null);

		// when
		when(postService.updateStatus(postId, mockRequest.status())).thenReturn(mockResponse);

		// Send PATCH request
		mockMvc.perform(patch("/api/v1/posts/{id}/status", postId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

  @Test
	@DisplayName("키워드를 포함하는 제목을 가진 게시글 전체 조회 성공")
	void GetPostByKeywordTest() throws Exception {
	    // given
	    String keyword = "Key";

	    // when then
		mockMvc.perform(get("/api/v1/posts/search")
			.param("keyword", keyword)
			.param("page", "0")
			.param("size", "10"))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("게시글 삭제 REST API 성공")
	public void deletePostTest() throws Exception {
		// given
		Long postId = 1L;
		// when then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/" + postId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
		verify(postService, times(1)).delete(postId);
	}

}
