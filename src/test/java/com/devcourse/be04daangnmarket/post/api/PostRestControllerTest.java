package com.devcourse.be04daangnmarket.post.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostRequest;
import com.devcourse.be04daangnmarket.post.dto.PostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class PostRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@MockBean
	MemberService memberService;

	@Test
	@DisplayName("게시글 등록 REST API 성공")
	void PostRestControllerTest() throws Exception {

		PostRequest request = new PostRequest("Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE);

		PostResponse mockResponse = new PostResponse(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE);

		when(postService.create("Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE)).thenReturn(mockResponse);

		// When-Then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.price").value(request.price()))
			.andExpect(jsonPath("$.views").value(request.views()))
			.andExpect(jsonPath("$.transactionType").value(request.transactionType().toString()))
			.andExpect(jsonPath("$.category").value(request.category().toString()))
			.andExpect(jsonPath("$.status").value(request.status().toString()));

	}

	@Test
	@DisplayName("게시글 단일 상세 조회 REST API 성공")
	public void getPostTest() throws Exception {
		// Given
		Long postId = 1L;
		PostResponse mockResponse = new PostResponse(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE);

		when(postService.getPost(1L)).thenReturn(mockResponse);

		// When and Then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/" + postId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Keyboard"))
			.andExpect(jsonPath("$.description").value("nice Keyboard"))
			.andExpect(jsonPath("$.price").value(100))
			.andExpect(jsonPath("$.views").value(1000))
			.andExpect(jsonPath("$.transactionType").value(TransactionType.SALE.toString()))
			.andExpect(jsonPath("$.category").value(Category.DIGITAL_DEVICES.toString()))
			.andExpect(jsonPath("$.status").value(Status.FOR_SALE.toString()));

	}

	@Test
	@DisplayName("게시글 수정 REST API 성공")
	public void updatePostTest() throws Exception {
		// Given
		Long postId = 1L;
		PostRequest request = new PostRequest("Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE);

		PostResponse mockResponse = new PostResponse(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE);

		when(postService.update(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE, Category.DIGITAL_DEVICES, Status.FOR_SALE)).thenReturn(mockResponse);

		// When-Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/" + postId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(postId))
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.price").value(request.price()))
			.andExpect(jsonPath("$.views").value(request.views()))
			.andExpect(jsonPath("$.transactionType").value(request.transactionType().toString()))
			.andExpect(jsonPath("$.category").value(request.category().toString()))
			.andExpect(jsonPath("$.status").value(request.status().toString()));

	}

	@Test
	@DisplayName("게시글 삭제 REST API 성공")
	public void deletePostTest() throws Exception {
		// Given
		Long postId = 1L;

		// When-Then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/" + postId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		verify(postService, times(1)).delete(postId);

	}

}
