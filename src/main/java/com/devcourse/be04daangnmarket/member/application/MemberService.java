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
    private final ProfileService profileService;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder, ProfileService profileService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileService = profileService;
    }

    public MemberDto.Response signUp(MemberDto.SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = MemberConverter.toEntity(request, encodedPassword);

        Member savedMember = memberRepository.save(member);
        profileService.create(savedMember.getId(), request.username(), request.region());

        return MemberConverter.toResponse(savedMember);
    }

    public MemberDto.Response kakaoSignUp(String email) {
        Member member = new Member(email);

        Member savedMember = memberRepository.save(member);
        profileService.create(savedMember.getId());

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

    public Member getOne(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
    }

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
