package com.devcourse.be04daangnmarket.comment.dto;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long commentId,

        Long memberId,

        String memberName,

        Long postId,

        String content,

        List<ImageResponse> images,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
