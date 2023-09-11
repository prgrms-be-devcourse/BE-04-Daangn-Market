package com.devcourse.be04daangnmarket.comment.dto;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {
    public record CreateCommentRequest(
            String content,

            Long postId,

            List<MultipartFile> files
    ) {
    }

    public record CreateReplyCommentRequest(
            String content,

            Long postId,

            int commentGroup,

            List<MultipartFile> files
    ) {
    }

    public record UpdateCommentRequest(
            String content,

            Long postId,

            List<MultipartFile> files
    ) {
    }

    public record CommentResponse(
            Long commentId,

            Long memberId,

            String username,

            Long postId,

            String content,

            List<ImageResponse> images,

            LocalDateTime createdAt,

            LocalDateTime updatedAt
    ) {
    }

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
}
