package com.devcourse.be04daangnmarket.comment.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateCommentRequest(
        String content,

        Long postId,

        List<MultipartFile> files
) {
}
