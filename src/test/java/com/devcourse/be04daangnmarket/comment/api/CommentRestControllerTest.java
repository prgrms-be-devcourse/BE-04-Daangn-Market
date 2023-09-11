package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.dto.CreateReplyCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.PostCommentResponse;
import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.devcourse.be04daangnmarket.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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

    @MockBean
    private CommentService commentService;

    @MockBean
    ImageService imageService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new User(new Member(
                                "username",
                                "0107209675",
                                "email@naver.com",
                                "1234")),
                        null,
                        Collections.emptyList()
                )
        );
    }

    @Test
    @WithMockUser
    void 댓글_저장_성공() throws Exception {
        //given
        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("예시", "예시.png", MediaType.IMAGE_JPEG_VALUE, "예시".getBytes());
        images.add(imageFile);
        CreateCommentRequest request = new CreateCommentRequest("댓글", 1L, null);

        List<ImageResponse> mockImageResponseList = new ArrayList<>();
        ImageResponse mockImageResponse = new ImageResponse("예시.png", "images/a8f468c1-d234-4c08-8235-63cc59f73a15-예시.png", "image/png",
                898066, DomainName.COMMENT, 1L);
        mockImageResponseList.add(mockImageResponse);

        CommentResponse mockResponse = new CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());

        given(imageService.uploadImages(
                eq(images),
                eq(DomainName.COMMENT),
                eq(1L)
        )).willReturn(mockImageResponseList);

        given(commentService.create(
                eq(request),
                eq(null),
                eq("username")
        )).willReturn(mockResponse);

        //when & then
        this.mockMvc.perform(multipart("/api/v1/comments")
                        .file(imageFile)
                        .param("postId", "1")
                        .param("content", "댓글"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 대댓글_저장_성공() throws Exception {
        //given
        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("예시", "예시.png", MediaType.IMAGE_JPEG_VALUE, "예시".getBytes());
        images.add(imageFile);
        CreateReplyCommentRequest request = new CreateReplyCommentRequest("댓글", 1L, 1, null);

        List<ImageResponse> mockImageResponseList = new ArrayList<>();
        ImageResponse mockImageResponse = new ImageResponse("예시.png", "images/a8f468c1-d234-4c08-8235-63cc59f73a15-예시.png", "image/png",
                898066, DomainName.COMMENT, 1L);
        mockImageResponseList.add(mockImageResponse);

        CommentResponse mockResponse = new CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());

        given(imageService.uploadImages(
                eq(images),
                eq(DomainName.COMMENT),
                eq(1L)
        )).willReturn(mockImageResponseList);

        given(commentService.createReply(
                eq(request),
                eq(null),
                eq("username")
        )).willReturn(mockResponse);

        //when & then
        this.mockMvc.perform(multipart("/api/v1/comments/reply")
                        .file(imageFile)
                        .param("postId", "1")
                        .param("content", "댓글")
                        .param("commentGroup", "1"))
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
        CommentResponse mockResponse = new CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());
        given(commentService.getDetail(commentId))
                .willReturn(mockResponse);

        //when & then
        mockMvc.perform(get("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 페이징_조회_성공() throws Exception {
        //given
        Long postId = 1L;
        PostCommentResponse mockResponse1 = new PostCommentResponse(1L, 1L, "username", 1L, "게시글", "댓글", null, null, LocalDateTime.now(), LocalDateTime.now());
        PostCommentResponse mockResponse2 = new PostCommentResponse(2L, 1L, "username", 1L, "게시글", "댓글2", null, null, LocalDateTime.now(), LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostCommentResponse> fakeResponses = List.of(mockResponse1, mockResponse2);
        Page<PostCommentResponse> responsePage = new PageImpl<>(fakeResponses, pageable, fakeResponses.size());

        given(commentService.getPostComments(1L, pageable))
                .willReturn(responsePage);

        //when & then
        mockMvc.perform(get("/api/v1/comments/page/{postId}", postId)
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
        UpdateCommentRequest request = new UpdateCommentRequest("댓글", 1L, null);
        CommentResponse mockResponse = new CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());

        given(commentService.update(
                eq(1L),
                eq(request),
                eq("username")
        )).willReturn(mockResponse);

        //when & then
        mockMvc.perform(put("/api/v1/comments/{id}", 1L)
                        .param("postId", "1")
                        .param("content", "댓글"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
