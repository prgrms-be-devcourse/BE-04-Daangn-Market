package com.devcourse.be04daangnmarket.comment.dto;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PostCommentResponse(
    Long commentId,

    Long memberId,

    String username,

    Long postId,

    String postTitle,

    String content,

    List<ImageResponse> images,

    List<CommentResponse> replyComments,

    LocalDateTime createdAt,

    LocalDateTime updatedAt
) {
}
