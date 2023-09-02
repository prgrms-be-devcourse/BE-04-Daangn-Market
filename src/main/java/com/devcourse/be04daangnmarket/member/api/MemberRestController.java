package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberDto.Response> signUp(@RequestBody MemberDto.SignUpRequest request) {
        MemberDto.Response response = memberService.signUp(request);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberDto.Response> signIn(@RequestBody MemberDto.SignInRequest request) {
        MemberDto.Response response = memberService.signIn(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.Response> getProfile(@PathVariable Long id) {
        MemberDto.Response response = memberService.getProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto.Response> updateProfile(@PathVariable Long id,
                                                            @RequestBody MemberDto.UpdateProfileRequest request) {
        MemberDto.Response response = memberService.updateProfile(id, request.username());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
