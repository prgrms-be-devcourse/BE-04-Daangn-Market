package com.devcourse.be04daangnmarket.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devcourse.be04daangnmarket.common.config.SecurityConfig;

@WebMvcTest(ImgTestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class ImgTestControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCreatePost() throws Exception {
		// Create a sample TestDto with a mock file
		MockMultipartFile file1 = new MockMultipartFile(
			"file",
			"test-file.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"This is a test file content".getBytes()
		);

		MockMultipartFile file2 = new MockMultipartFile(
			"file",
			"test-file.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"This is a test file content".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/create-img")
				.file(file1)
				.file(file2)
				.param("name", "John"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

}