package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.beombu.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(CreateCommentRequest dto) {
        return new Comment(dto.content());
    }

    public static CommentResponse toResponse(Comment comment, List<ImageResponse> images) {
        return new CommentResponse(comment.getContent(), images);
    }
}
