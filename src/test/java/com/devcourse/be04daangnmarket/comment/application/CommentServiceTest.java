package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.domain.DeletedStatus;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage.NOT_FOUND_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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
        assertThatThrownBy(() -> commentService.delete(commentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NOT_FOUND_COMMENT.getMessage());
    }

    @Test
    void 삭제시_삭제상태변경이_확인() {
        //given
        Comment comment = new Comment("댓글");

        //when
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        commentService.delete(comment.getId());

        // then
        assertThat(comment.getDeletedStatus()).isEqualTo(DeletedStatus.DELETED);

    }
}
