package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    MemberDto.SignUpRequest request;
    MemberDto.Response response;

    @BeforeEach
    void setUp() {
        String username = "kys0411";
        String phoneNumber = "01012341234";
        String email = "ys990411@naver.com";
        String password = "1234";

        request = new MemberDto.SignUpRequest(username, phoneNumber, email, password);
        response = memberService.signUp(request);
    }

    @AfterEach
    void deleteAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() {
        Assertions.assertThat(response.id()).isNotNull();
        Assertions.assertThat(response.username()).isEqualTo("kys0411");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void sign_in_success() {
        String email = "ys990411@naver.com";
        String password = "1234";

        MemberDto.SignInRequest loginRequest = new MemberDto.SignInRequest(email, password);
        MemberDto.Response loginResponse = memberService.signIn(loginRequest);

        Assertions.assertThat(loginResponse.id()).isEqualTo(response.id());
        Assertions.assertThat(loginResponse.email()).isEqualTo(response.email());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호를 잘못 입력하였을 경우")
    void sign_in_fail() {
        String email = "ys990411@naver.com";
        String password = "999999";

        MemberDto.SignInRequest loginRequest = new MemberDto.SignInRequest(email, password);

        Assertions.assertThatThrownBy(() -> memberService.signIn(loginRequest))
                        .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 이메일을 잘못 입력하였을 경우")
    void sign_in_fail2() {
        String email = "fail@naver.com";
        String password = "1234";

        MemberDto.SignInRequest loginRequest = new MemberDto.SignInRequest(email, password);

        Assertions.assertThatThrownBy(() -> memberService.signIn(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("닉네임 수정 테스트 - 성공")
    void update_success() {
        String username = "abcd111";

        MemberDto.Response updateResponse = memberService.updateProfile(response.id(), username);

        Assertions.assertThat(updateResponse.username()).isEqualTo(username);
    }

    @Test
    @DisplayName("닉네임 수정 실패 테스트 - 중복된 닉네임을 입력하였을 경우")
    void update_fail() {
        String username = "kys0411";

        Assertions.assertThatThrownBy(() -> memberService.updateProfile(response.id(), username))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유저의 프로필 조회 테스트 - 성공")
    void get_profile() {
        MemberDto.Response profile = memberService.getProfile(response.id());

        Assertions.assertThat(profile.id()).isEqualTo(response.id());
        Assertions.assertThat(profile.email()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("유저의 프로필 조회 실패 테스트 - 존재하지 않는 id일 경우")
    void get_profile_fail() {
        Assertions.assertThatThrownBy(() -> memberService.getProfile(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
