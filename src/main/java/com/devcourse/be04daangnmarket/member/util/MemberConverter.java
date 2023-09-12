package com.devcourse.be04daangnmarket.member.util;

import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberConverter {
    public static Member toEntity(MemberDto.SignUpRequest request, PasswordEncoder passwordEncoder) {
        return new Member(
                request.username(),
                request.phoneNumber(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
    }

    public static MemberDto.Response toResponse(Member member) {
        return new MemberDto.Response(
                member.getId(),
                member.getUsername(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getTemperature(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
