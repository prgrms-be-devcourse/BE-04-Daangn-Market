package com.devcourse.be04daangnmarket.member.view;

import com.devcourse.be04daangnmarket.member.application.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignViewController {
    private final KakaoService kakaoService;

    public SignViewController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping(value = "/sign-up")
    public String signUp() {
        return "signUp";
    }

    @GetMapping(value = "/sign-in")
    public String signIn(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());

        return "signIn";
    }
}
