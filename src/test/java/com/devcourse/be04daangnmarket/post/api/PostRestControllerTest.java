package com.devcourse.be04daangnmarket.post.api;

import static org.mockito.Mockito.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PostRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class PostRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@Test
	@DisplayName("게시글 등록 REST API 성공")
	void createPostTest() throws Exception {

		PostDto.CreateRequest request = new PostDto.CreateRequest("Keyboard", "nice Keyboard", 100, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		PostDto.Response mockResponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(), Status.FOR_SALE.getDescription());

		when(postService.create("Keyboard", "nice Keyboard", 100,
			TransactionType.SALE, Category.DIGITAL_DEVICES)).thenReturn(mockResponse);

		// when then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.price").value(request.price()))
			.andExpect(jsonPath("$.transactionType").value(request.transactionType().getDescription()))
			.andExpect(jsonPath("$.category").value(request.category().getDescription()));

	}

	@Test
	@DisplayName("게시글 단일 상세 조회 REST API 성공")
	public void getPostTest() throws Exception {
		// given
		Long postId = 1L;
		PostDto.Response mockResponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(), Status.FOR_SALE.getDescription());

		when(postService.getPost(1L)).thenReturn(mockResponse);

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
		PostDto.Response postResponse1 = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(), Status.FOR_SALE.getDescription());

		PostDto.Response postResponse2 = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(), Status.FOR_SALE.getDescription());

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
	@DisplayName("게시글 수정 REST API 성공")
	public void updatePostTest() throws Exception {
		// given
		Long postId = 1L;
		PostDto.UpdateRequest request = new PostDto.UpdateRequest("Keyboard", "nice Keyboard", 100, TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		PostDto.Response mockResponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(), Status.FOR_SALE.getDescription());

		when(postService.update(1L, "Keyboard", "nice Keyboard", 100,
			TransactionType.SALE, Category.DIGITAL_DEVICES)).thenReturn(mockResponse);

		// when then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/" + postId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(postId))
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.price").value(request.price()))
			.andExpect(jsonPath("$.transactionType").value(request.transactionType().getDescription()))
			.andExpect(jsonPath("$.category").value(request.category().getDescription()));

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
