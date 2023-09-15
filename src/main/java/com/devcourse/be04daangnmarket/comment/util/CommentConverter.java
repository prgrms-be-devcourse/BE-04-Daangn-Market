package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.comment.domain.Comment;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CommentDto.CreateCommentRequest dto, Long postId, Long userId) {
        return new Comment(dto.content(), postId, userId);
    }

    public static Comment toEntity(CommentDto.CreateReplyCommentRequest dto, Long postId, Long userId) {
        return new Comment(dto.content(), userId, postId, dto.commentGroup());
    }

    public static CommentDto.CommentResponse toResponse(Comment comment, List<ImageDto.ImageResponse> images, String username) {
        return new CommentDto.CommentResponse(
                comment.getId(),
                comment.getMemberId(),
                username,
                comment.getPostId(),
                comment.getContent(),
                images,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static CommentDto.PostCommentResponse toResponse(Comment comment, String commentUsername, String postTitle, List<ImageDto.ImageResponse> commentImages, List<CommentDto.CommentResponse> replyCommentResponses) {
        return new CommentDto.PostCommentResponse(
                comment.getId(),
                comment.getMemberId(),
                commentUsername,
                comment.getPostId(),
                postTitle,
                comment.getContent(),
                commentImages,
                replyCommentResponses,
                comment.getCommentGroup(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
