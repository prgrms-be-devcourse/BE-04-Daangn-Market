package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.comment.dto.CreateReplyCommentRequest;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CreateCommentRequest dto, Long userId) {
        return new Comment(dto.content(), userId, dto.postId());
    }

    public static Comment toEntity(CreateReplyCommentRequest dto, Long userId) {
        return new Comment(dto.content(), userId, dto.postId(), dto.commentGroup());
    }

    public static CommentResponse toResponse(Comment comment, List<ImageResponse> images, String username) {
        return new CommentResponse(comment.getId(), comment.getMemberId(), username,
                comment.getPostId(), comment.getContent(), images, comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
