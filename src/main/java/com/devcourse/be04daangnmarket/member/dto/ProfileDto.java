package com.devcourse.be04daangnmarket.member.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ProfileDto {
    public record UpdateRequest(
            @NotBlank(message = "닉네임은 필수로 입력하여야 합니다.")
            String username
    ) {
    }

    public record Response(
            Long memberId,

            String username,

            String region,

            double temperature,

            LocalDateTime createdAt
    ) {
    }
}
