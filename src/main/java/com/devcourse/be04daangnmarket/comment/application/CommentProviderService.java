package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentProviderService {
    Comment getComment(Long id);

    Page<CommentDto.PostCommentResponse> getPostComments(Long postId, Pageable pageable);

    Page<MemberDto.Response> getCommenterByPostId(Long writerId, Pageable pageable);
}
