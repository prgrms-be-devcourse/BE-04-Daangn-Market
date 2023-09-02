package com.devcourse.be04daangnmarket.member.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping(value = "/mypage/{id}")
    public String mypage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);

        return "mypage";
    }

    @GetMapping(value = "/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);

        return "profile";
    }

    @GetMapping(value = "/profile/edit/{id}")
    public String editProfile(@PathVariable Long id, Model model) {

        model.addAttribute("id", id);

        return "editProfile";
    }

}
