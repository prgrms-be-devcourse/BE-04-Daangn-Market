package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto.Response signUp(MemberDto.SignUpRequest request) {
        Member member = new Member(
                request.username(),
                request.phoneNumber(),
                request.email(),
                request.password()
        );

        Member savedMember = memberRepository.save(member);

        return toResponse(savedMember);
    }

    private MemberDto.Response toResponse(Member member) {
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
