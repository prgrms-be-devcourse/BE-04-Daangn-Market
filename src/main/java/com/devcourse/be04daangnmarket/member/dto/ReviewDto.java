package com.devcourse.be04daangnmarket.member.dto;

import com.devcourse.be04daangnmarket.member.domain.Review.WriterRole;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ReviewDto {
    public record CreateRequest(
            @NotBlank(message = "내용은 필수로 입력하여야 합니다.")
            String content
    ){
    }

    public record Response(
            Long id,

            Long ownerId,

            Long postId,

            Long writerId,

            WriterRole writerRole,

            String content,

            LocalDateTime createdAt,

            LocalDateTime updatedAt
    ) {
    }
}
