package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CommentDto.CreateCommentRequest dto, Long userId) {
        return new Comment(dto.content(), userId, dto.postId());
    }

    public static Comment toEntity(CommentDto.CreateReplyCommentRequest dto, Long userId) {
        return new Comment(dto.content(), userId, dto.postId(), dto.commentGroup());
    }

    public static CommentDto.CommentResponse toResponse(Comment comment, List<ImageResponse> images, String username) {
        return new CommentDto.CommentResponse(comment.getId(), comment.getMemberId(), username,
                comment.getPostId(), comment.getContent(), images, comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
