package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class CommentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void 댓글_저장_성공() throws Exception {
        //given
        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("구직_공고문", "구직_공고문.png", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        images.add(imageFile);
        CreateCommentRequest request = new CreateCommentRequest("댓글", 1L, images);

        List<ImageResponse> imageResponseList1 = new ArrayList<>();
        ImageResponse imageResponse1 = new ImageResponse("구직_공고문.png", "images/a8f468c1-d234-4c08-8235-63cc59f73a15-구직_공고문.png", "image/png",
                898066, DomainName.COMMENT, 1L);
        imageResponseList1.add(imageResponse1);

        CommentResponse mockResponse = new CommentResponse("댓글", 1L, 1L, 1, 0, imageResponseList1);

//        given(imageService.uploadImages(images, DomainName.COMMENT, 1L))
//                .willReturn(imageResponses);
        given(commentService.create(request, 1L))
                .willReturn(mockResponse);

        //when & then
        this.mockMvc.perform(multipart("/api/v1/comments")
                        .file(imageFile)
                        .param("postId", "1L")
                        .param("content", "댓글"))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    void 삭제_성공() throws Exception {
        //given
        Long commentId = 1L;

        //when & then
        mockMvc.perform(delete("/api/v1/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void 조회_성공() throws Exception {
        //given
        Long commentId = 1L;
        CommentResponse response = new CommentResponse("댓글", 1L, 1L, 1, 0, null);
        given(commentService.getDetail(commentId))
                .willReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 페이징_조회_성공() throws Exception {
        //given
        CommentResponse response1 = new CommentResponse("댓글", 1L, 1L, 1, 0, null);
        CommentResponse response2 = new CommentResponse("댓글", 1L, 1L, 1, 0, null);

        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<CommentResponse> responses = new PageImpl<>(List.of(response1, response2), pageable, 2);
        given(commentService.getPage(pageable))
                .willReturn(responses);

        mockMvc.perform(get("/api/v1/comments")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "createdAt.desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 수정_성공() throws Exception {
        //given
        Long commentId = 1L;
        CommentResponse response = new CommentResponse("댓글", 1L, 1L, 1, 0, null);

        UpdateCommentRequest request = new UpdateCommentRequest("변경 댓글");
        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "request", "application/json", requestJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile imageFile = new MockMultipartFile("test", "test.jpeg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        List<MultipartFile> images = new ArrayList<>();
        images.add(imageFile);

        //when
        when(commentService.update(commentId, request, images)).thenReturn(response);

        //then
        mockMvc.perform(put("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
