package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberRestController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberRestController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<MemberDto.Response> signUp(@RequestBody @Valid MemberDto.SignUpRequest request) {
        MemberDto.Response response = memberService.signUp(request);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberDto.Response> signIn(@RequestBody @Valid MemberDto.SignInRequest request) {
        MemberDto.Response response = memberService.signIn(request);

        String token = jwtTokenProvider.createToken(request.email());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);
        return new ResponseEntity(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.Response> getProfile(@PathVariable Long id) {
        MemberDto.Response response = memberService.getProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto.Response> updateProfile(@AuthenticationPrincipal User user,
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid MemberDto.UpdateProfileRequest request) {
        memberService.validateById(id, user.getId());
        MemberDto.Response response = memberService.updateProfile(id, request.username());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
