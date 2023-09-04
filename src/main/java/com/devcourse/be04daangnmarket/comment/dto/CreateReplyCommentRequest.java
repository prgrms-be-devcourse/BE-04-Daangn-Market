package com.devcourse.be04daangnmarket.comment.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateReplyCommentRequest(
        String content,

        Long postId,

        int commentGroup,

        List<MultipartFile> files
) {
}
