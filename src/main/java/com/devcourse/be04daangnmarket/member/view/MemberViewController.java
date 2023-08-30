package com.devcourse.be04daangnmarket.member.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    @GetMapping(value = "/sign-up")
    public String signUp() {
        return "signUp";
    }

    @GetMapping(value = "/sign-in")
    public String signIn() {
        return "signIn";
    }
}
