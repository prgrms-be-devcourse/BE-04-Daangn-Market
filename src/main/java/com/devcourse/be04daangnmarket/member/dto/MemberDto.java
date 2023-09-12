package com.devcourse.be04daangnmarket.member.dto;

import com.devcourse.be04daangnmarket.common.constant.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class MemberDto {
    public record SignUpRequest(
            @NotBlank(message = "닉네임은 필수로 입력하여야 합니다.")
            String username,

            @NotBlank(message = "전화번호는 필수로 입력하여야 합니다.")
            @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식에 맞게 입력해주세요.")
            String phoneNumber,

            @NotBlank(message = "이메일은 필수로 입력하여야 합니다.")
            @Email(message = "이메일 형식이 잘못되었습니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수로 입력하여야 합니다.")
            @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 작성하여야 합니다.")
            String password
    ) {
    }

    public record SignInRequest(
            @NotBlank(message = "이메일은 필수로 입력하여야 합니다.")
            @Email(message = "이메일 형식이 잘못되었습니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수로 입력하여야 합니다.")
            String password
    ) {
    }

    public record UpdateProfileRequest(
            @NotBlank(message = "닉네임은 필수로 입력하여야 합니다.")
            String username
    ) {
    }

    public record Response(
            Long id,

            String username,

            String phoneNumber,

            String email,

            double temperature,

            Status status,

            LocalDateTime createdAt,

            LocalDateTime updatedAt
    ) {
    }
}
