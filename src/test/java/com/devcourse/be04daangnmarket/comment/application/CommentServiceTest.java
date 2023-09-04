package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.domain.DeletedStatus;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage.NOT_FOUND_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void 삭제시_id가_없으면_예외() {
        //given
        Long commentId = 9L;

        //when & then
        assertThatThrownBy(() -> commentService.deleteReply(commentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NOT_FOUND_COMMENT.getMessage());
    }

    @Test
    void 삭제시_삭제상태변경_확인() {
        //given
        Comment comment = new Comment("댓글");
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        //when
        commentService.deleteReply(comment.getId());

        // then
        assertThat(comment.getDeletedStatus()).isEqualTo(DeletedStatus.DELETED);
    }

    @Test
    void 페이징_조회_성공_테스트() {
        //given
        Comment comment1 = new Comment("댓글");
        Comment comment2 = new Comment("댓글");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = new PageImpl<>(List.of(comment1, comment2), pageable, 2);

        //when
        when(commentRepository.findAll(pageable)).thenReturn(comments);
        Page<CommentResponse> responses = commentService.getPage(pageable);

        //then
        assertThat(comments.getSize()).isEqualTo(responses.getSize());
        assertThat(comments.getTotalElements()).isEqualTo(responses.getTotalElements());
    }

    @Test
    void 수정_성공() {
        //given
        UpdateCommentRequest request = new UpdateCommentRequest("변경댓글");
        Comment comment = new Comment("이전댓글");

        //when
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        commentService.update(comment.getId(), request, null);

        //then
        assertThat(comment.getContent()).isEqualTo(request.content());
    }
}
