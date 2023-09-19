package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import com.devcourse.be04daangnmarket.member.util.MemberConverter;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.FAIL_LOGIN;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.ILLEGAL_USER_ACCESS;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.NOT_FOUND_USER;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberDto.Response signUp(MemberDto.SignUpRequest request) {
        Member member = MemberConverter.toEntity(request, passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return MemberConverter.toResponse(savedMember);
    }

    public MemberDto.Response kakaoSignUp(String email) {
        Member member = new Member(email);

        Member savedMember = memberRepository.save(member);

        return MemberConverter.toResponse(savedMember);
    }

    public MemberDto.Response signIn(MemberDto.SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException(FAIL_LOGIN.getMessage()));

        if (member.isMatchedPassword(passwordEncoder, request.password())) {
            return MemberConverter.toResponse(member);
        }

        throw new UsernameNotFoundException(FAIL_LOGIN.getMessage());
    }

    public MemberDto.Response kakaoSignIn(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(FAIL_LOGIN.getMessage()));

        return MemberConverter.toResponse(member);
    }

    public void validateById(Long pathId, Long authUserId) {
        if (!pathId.equals(authUserId)) {
            throw new IllegalArgumentException(ILLEGAL_USER_ACCESS.getMessage());
        }
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
    }

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
