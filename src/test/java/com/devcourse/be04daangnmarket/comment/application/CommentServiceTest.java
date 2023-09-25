package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.common.image.dto.Type;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
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
    private MemberService memberService;

    @Mock
    private PostService postService;

    private Member member = new Member("username", "010-1111-1111", "sunil13@naver.com", "11111111");
    private Post post = new Post(1L, "제목", "내용", 100, TransactionType.SALE, Category.DIGITAL_DEVICES);

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
        Comment comment = new Comment("댓글", member, post);
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
        Comment comment1 = new Comment("댓글", member, post, 1);
        Comment comment2 = new Comment("댓글", member, post, 2);

        List<Comment> comments = List.of(comment1, comment2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentPage = new PageImpl<>(comments);
        given(commentRepository.findAllByPostIdToSeqIsZero(postId)).willReturn(comments);

        Member member = new Member("username", "번호", "이메일", "1234");
        given(memberService.getMember(memberId)).willReturn(member);

        Post post = new Post(memberId, "제목", "설명", 100, TransactionType.SALE, Category.DIGITAL_DEVICES);
        given(postService.findPostById(postId)).willReturn(post);

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
        ImageDto.ImageDetail imageDetail = new ImageDto.ImageDetail("test1", "uniqueName-test1.png", Type.PNG);
        List<ImageDto.ImageDetail> imageDetails = List.of(imageDetail);

        Comment comment = new Comment("수정한댓글", member, post);
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        //when
        commentService.update(comment.getId(), comment.getPostId(), "username", comment.getContent(), imageDetails);

        //then
        assertThat(comment.getContent()).isEqualTo("수정한댓글");
    }
}
