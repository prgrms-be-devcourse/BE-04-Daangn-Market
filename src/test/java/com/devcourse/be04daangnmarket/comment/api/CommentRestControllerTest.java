package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void 저장_성공() throws Exception {
        //given
        CreateCommentRequest request = new CreateCommentRequest("댓글");
        CommentResponse response = new CommentResponse("댓글");

        given(commentService.create(request))
                .willReturn(response);

        //when & then
        this.mockMvc.perform(post("/api/v1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
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
        CommentResponse response = new CommentResponse("댓글");
        given(commentService.getDetail(commentId))
                .willReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/comments/{id}", commentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
