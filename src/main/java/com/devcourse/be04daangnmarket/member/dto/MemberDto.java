package com.devcourse.be04daangnmarket.member.dto;

import com.devcourse.be04daangnmarket.member.domain.constant.Status;

import java.time.LocalDateTime;

public class MemberDto {

    public record SignUpRequest(
            String username,
            String phoneNumber,
            String email,
            String password
    ) { }

    public record SignInRequest(
            String email,
            String password
    ) { }

    public record UpdateProfileRequest(
            String username
    ) { }

    public record Response(
            Long id,
            String username,
            String phoneNumber,
            String email,
            double temperature,
            Status status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) { }
}
