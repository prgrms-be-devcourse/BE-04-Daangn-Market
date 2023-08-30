package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() {
        String username = "kys0411";
        String phoneNumber = "01012341234";
        String email = "ys990411@naver.com";
        String password = "1234";

        MemberDto.SignUpRequest request = new MemberDto.SignUpRequest(username, phoneNumber, email, password);
        MemberDto.Response response = memberService.signUp(request);

        Assertions.assertThat(response.id()).isNotNull();
        Assertions.assertThat(response.username()).isEqualTo("kys0411");
    }
}