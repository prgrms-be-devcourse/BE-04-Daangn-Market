package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.member.exception.DuplicatedUsernameException;
import com.devcourse.be04daangnmarket.member.exception.NotFoundMemberException;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.DUPLICATED_USERNAME;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.FAIL_LOGIN;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.ILLEGAL_USER_ACCESS;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.NOT_FOUND_USER;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberDto.Response signUp(MemberDto.SignUpRequest request) {
        Member member = new Member(
                request.username(),
                request.phoneNumber(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        Member savedMember = memberRepository.save(member);

        return toResponse(savedMember);
    }

    public MemberDto.Response signIn(MemberDto.SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException(FAIL_LOGIN.getMessage()));

        if (member.isMatchedPassword(passwordEncoder, request.password())) {
            return toResponse(member);
        }

        throw new UsernameNotFoundException(FAIL_LOGIN.getMessage());
    }

    public MemberDto.Response updateProfile(Long id, String username) {
        Member member = getMember(id);

        if (isAvailableUsername(username)) {
            member.updateProfile(username);
            return toResponse(member);
        }

        throw new DuplicatedUsernameException(DUPLICATED_USERNAME.getMessage());
    }

    public MemberDto.Response getProfile(Long id) {
        Member member = getMember(id);

        return toResponse(member);
    }

    public void validateById(Long pathId, Long authUserId) {
        if (pathId != authUserId) {
            throw new IllegalArgumentException(ILLEGAL_USER_ACCESS.getMessage());
        }
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException(NOT_FOUND_USER.getMessage()));
    }

    private boolean isAvailableUsername(String username) {
        return memberRepository.findByUsername(username).isEmpty();
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
