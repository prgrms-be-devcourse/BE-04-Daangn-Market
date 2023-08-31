package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;

public class CommentConverter {
    public static Comment toEntity(CreateCommentRequest dto) {
        return new Comment(dto.comment());
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getContent());
    }
}
