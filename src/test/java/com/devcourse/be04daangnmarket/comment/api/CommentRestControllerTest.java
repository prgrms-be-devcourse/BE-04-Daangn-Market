package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.image.LocalImageUpload;
import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.common.config.SecurityConfig;
import com.devcourse.be04daangnmarket.common.image.dto.Type;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private ImageService imageService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private LocalImageUpload imageUpload;

    private Member member;
    private Post post;

    @BeforeEach
    public void setup() {
        //new Post(1L, "제목", "내용", )
        member = new Member("01011111111", "email@naver.com", "1234");
        User user = new User(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithMockUser
    void 댓글_저장_성공() throws Exception {
        //given
        byte[] bytes = "test1".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile imageFile = new MockMultipartFile("test1", "test1.png", MediaType.TEXT_PLAIN_VALUE, bytes);

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(imageFile);

        String uniqueName = UUID.randomUUID() + ".png";
        ImageDto.ImageDetail imageDetail = new ImageDto.ImageDetail("test1", uniqueName, Type.PNG);
        List<ImageDto.ImageDetail> imageDetails = List.of(imageDetail);

        List<String> pathLists = List.of(uniqueName);
        CommentDto.CommentResponse mockResponse = new CommentDto.CommentResponse(1L, 1L, "username", 1L, "댓글", pathLists, LocalDateTime.now(), LocalDateTime.now());

        given(imageUpload.uploadImages(multipartFiles)).willReturn(imageDetails);

        given(commentService.create(
                1L,
                1L,
                "username",
                "댓글",
                imageDetails
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
        List<MultipartFile> imagePaths = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("예시", "예시.png", MediaType.IMAGE_JPEG_VALUE, "예시".getBytes());
        imagePaths.add(imageFile);

        ImageDto.ImageDetail imageDetail = new ImageDto.ImageDetail("test1", "uniqueName-test1.png", Type.PNG);
        List<ImageDto.ImageDetail> imageDetails = List.of(imageDetail);

        List<String> pathLists = List.of("images/uniqueName-test1.png");
        CommentDto.CommentResponse mockResponse = new CommentDto.CommentResponse(1L, 1L, "username", 1L, "댓글", pathLists, LocalDateTime.now(), LocalDateTime.now());

        given(imageUpload.uploadImages(any())).willReturn(imageDetails);

        given(commentService.createReply(
                eq(1L),
                eq(1L),
                eq("username"),
                eq(1),
                eq("댓글"),
                eq(imageDetails)
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
        CommentDto.CommentResponse mockResponse = new CommentDto.CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());
        given(commentService.getDetail(commentId))
                .willReturn(mockResponse);

        //when & then
        mockMvc.perform(get("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void 수정_성공() throws Exception {
        //given
        CommentDto.CommentResponse mockResponse = new CommentDto.CommentResponse(1L, 1L, "username", 1L, "댓글", null, LocalDateTime.now(), LocalDateTime.now());

        given(commentService.update(
                eq(1L),
                eq(1L),
                eq("username"),
                eq("댓글"),
                eq(null)
        )).willReturn(mockResponse);

        //when & then
        mockMvc.perform(put("/api/v1/comments/{id}", 1L)
                        .param("postId", "1")
                        .param("content", "댓글"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
