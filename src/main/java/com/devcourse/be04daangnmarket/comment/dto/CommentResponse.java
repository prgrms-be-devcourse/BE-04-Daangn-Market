package com.devcourse.be04daangnmarket.comment.dto;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;

import java.util.List;

public record CommentResponse(
        String content,

        Long memberId,

        Long postId,

        int group,

        int seq,

        List<ImageResponse> images
) {
}
