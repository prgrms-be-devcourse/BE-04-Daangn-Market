package com.devcourse.be04daangnmarket.post.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

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
	public void Test() throws Exception {

		PostDto.CreateRequest request = new PostDto.CreateRequest("Keyboard", "nice Keyboard",
			100, TransactionType.SALE, Category.DIGITAL_DEVICES);
		String requestJson = objectMapper.writeValueAsString(request);
		MockMultipartFile jsonFile = new MockMultipartFile("request", "request",
			"application/json", requestJson.getBytes(StandardCharsets.UTF_8));

		List<MultipartFile> images = new ArrayList<>();
		MockMultipartFile imageFile = new MockMultipartFile("images", "test-image.jpg",
			MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
		images.add(imageFile);

		PostDto.Response mockresponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

		when(postService.create("Keyboard", "nice Keyboard", 100,
			TransactionType.SALE, Category.DIGITAL_DEVICES, images)).thenReturn(mockresponse);

		// 요청 생성
		mockMvc.perform(
				multipart("/api/v1/posts")
					.file(jsonFile)
					.file("request", requestJson.getBytes())
					.file(imageFile)
			)
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

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
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

		PostDto.Response postResponse2 = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

		List<PostDto.Response> mockResponses = List.of(postResponse1, postResponse2);

		Pageable pageable = PageRequest.of(0, 10);
		Page<PostDto.Response> responsePage = new PageImpl<>(mockResponses, pageable, mockResponses.size());

		when(postService.getAllPost(pageable)).thenReturn(responsePage);

		// when then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(mockResponses.size()));

	}

	@Test
	@DisplayName("게시글 카테고리 기반 전체 조회 REST API 성공")
	public void testGetPostByCategory() throws Exception {
		// given
		Category category = Category.DIGITAL_DEVICES;
		PageRequest pageable = PageRequest.of(0, 10);

		PostDto.Response postResponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

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
	@DisplayName("게시글 수정 REST API 성공")
	public void updatePostTest() throws Exception {
		// given
		Long postId = 1L;
		PostDto.UpdateRequest request = new PostDto.UpdateRequest("Keyboard", "nice Keyboard", 100,
			TransactionType.SALE,
			Category.DIGITAL_DEVICES);

		PostDto.Response mockResponse = new PostDto.Response(1L, "Keyboard", "nice Keyboard", 100, 1000,
			TransactionType.SALE.getDescription(), Category.DIGITAL_DEVICES.getDescription(),
			Status.FOR_SALE.getDescription(), List.of("img_path"));

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
