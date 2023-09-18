package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.member.application.KakaoService;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.dto.KakaoResponse;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class SignRestController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public SignRestController(KakaoService kakaoService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<MemberDto.Response> kakaoLogin(@RequestParam(required = false) String code) throws Exception {
        KakaoResponse kakaoInfo = kakaoService.getKakaoInfo(code);

        String username = kakaoInfo.username();
        String email = kakaoInfo.email();

        if (memberService.isExistMember(email)) {
            MemberDto.Response response = memberService.kakaoSignIn(email);
            String token = jwtTokenProvider.createToken(email);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", token);

            return ResponseEntity.status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(response);
        }

        MemberDto.Response response = memberService.kakaoSignUp(username, email);

        return ResponseEntity.ok(response);
    }
}
