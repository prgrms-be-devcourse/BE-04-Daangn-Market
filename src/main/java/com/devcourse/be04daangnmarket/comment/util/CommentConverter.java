package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CreateCommentRequest dto, Long userId) {
        return new Comment(dto.content(), userId, dto.postId(), 0);
    }

    public static CommentResponse toResponse(Comment comment, List<ImageResponse> images) {
        return new CommentResponse(comment.getContent(), comment.getMemberId(),
                comment.getPostId(), comment.getCommentGroup(), comment.getSeq(), images);
    }
}
