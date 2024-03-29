package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentProviderService {
    Comment getComment(Long id);

    Page<CommentDto.PostCommentResponse> getPostComments(Long postId, Pageable pageable);

    Page<ProfileDto.Response> getCommenterByPostId(Long postId, Long writerId, Pageable pageable);
}
