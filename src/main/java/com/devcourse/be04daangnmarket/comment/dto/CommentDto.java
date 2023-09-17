package com.devcourse.be04daangnmarket.comment.dto;

import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {
    public record CreateCommentRequest(
            @NotNull
            @Size(max = 500, message = "댓글의 최대 길이는 500자까지 입니다.")
            String content,

            @NotBlank(message = "게시글의 Id는 필수입니다.")
            Long postId,

            List<MultipartFile> files
    ) {
    }

    public record CreateReplyCommentRequest(
            @NotNull
            @Size(max = 500, message = "댓글의 최대 길이는 500자까지 입니다.")
            String content,

            @NotBlank(message = "게시글의 Id는 필수입니다.")
            Long postId,

            @NotBlank(message = "부모 댓글의 Id는 필수입니다.")
            int commentGroup,

            List<MultipartFile> files
    ) {
    }

    public record UpdateCommentRequest(
            @NotNull
            @Size(max = 500, message = "댓글의 최대 길이는 500자까지 입니다.")
            String content,

            @NotBlank(message = "게시글의 Id는 필수입니다.")
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

            List<ImageDto.ImageResponse> images,

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

            List<ImageDto.ImageResponse> images,

            List<CommentResponse> replyComments,

            int commentGroup,

            LocalDateTime createdAt,

            LocalDateTime updatedAt
    ) {
    }
}
