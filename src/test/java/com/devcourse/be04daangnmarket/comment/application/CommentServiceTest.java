package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.devcourse.be04daangnmarket.comment.exception.ErrorMessage.NOT_FOUND_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private ImageService imageService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Test
    void 삭제시_id가_없으면_예외() {
        //given
        Long commentId = 9L;

        //when & then
        assertThatThrownBy(() -> commentService.delete(commentId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_FOUND_COMMENT.getMessage());
    }

    @Test
    void 삭제시_삭제상태변경_확인() {
        //given
        Comment comment = new Comment("댓글", 1L, 1L);
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        //when
        commentService.delete(comment.getId());

        // then
        assertThat(comment.getStatus()).isEqualTo(Status.DELETED);
    }

    @Test
    void 페이징_조회_성공_테스트() {
        //given
        Long memberId = 1L;
        Long postId = 1L;
        Comment comment1 = new Comment("댓글", memberId, postId, 1);
        Comment comment2 = new Comment("댓글", memberId, postId, 2);

        List<Comment> comments = List.of(comment1, comment2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentPage = new PageImpl<>(comments);
        given(commentRepository.findAllByPostIdToSeqIsZero(postId)).willReturn(comments);

        Member member = new Member("username", "번호", "이메일", "1234");
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        Post post = new Post(memberId, "제목", "설명", 100, TransactionType.SALE, Category.DIGITAL_DEVICES);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        //when
        Page<CommentDto.PostCommentResponse> responses = commentService.getPostComments(postId, pageable);

        //then
        assertThat(commentPage.getTotalElements()).isEqualTo(responses.getTotalElements());
        assertThat(responses.getContent().get(0).content()).isEqualTo("댓글");
        assertThat(responses.getContent()).hasSize(2);
        verify(commentRepository, times(1)).findAllByPostIdToSeqIsZero(postId);
    }

    @Test
    void 수정_성공() {
        //given
        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("예시", "예시.png", MediaType.IMAGE_JPEG_VALUE, "예시".getBytes());
        images.add(imageFile);

        CommentDto.UpdateCommentRequest request = new CommentDto.UpdateCommentRequest("변경댓글", 1L, images);
        Comment comment = new Comment("이전댓글", 1L, 1L);
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        //when
        commentService.update(comment.getId(), request, "username");

        //then
        assertThat(comment.getContent()).isEqualTo(request.content());
    }
}
