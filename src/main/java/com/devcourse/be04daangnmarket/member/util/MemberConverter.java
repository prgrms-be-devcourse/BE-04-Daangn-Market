package com.devcourse.be04daangnmarket.member.util;

import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;

public class MemberConverter {
    public static Member toEntity(MemberDto.SignUpRequest request, String encodedPassword) {
        return new Member(
                request.phoneNumber(),
                request.email(),
                encodedPassword
        );
    }

    public static MemberDto.Response toResponse(Member member) {
        return new MemberDto.Response(
                member.getId(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
