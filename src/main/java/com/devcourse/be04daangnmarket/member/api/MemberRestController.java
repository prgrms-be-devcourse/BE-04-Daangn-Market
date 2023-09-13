package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.jwt.JwtTokenProvider;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import jakarta.validation.Valid;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberRestController {
    private final int PAGE_SIZE = 5;

    private final MemberService memberService;
    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberRestController(MemberService memberService, PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<MemberDto.Response> signUp(@RequestBody @Valid MemberDto.SignUpRequest request) {
        MemberDto.Response response = memberService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberDto.Response> signIn(@RequestBody @Valid MemberDto.SignInRequest request) {
        MemberDto.Response response = memberService.signIn(request);

        String token = jwtTokenProvider.createToken(request.email());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.Response> getProfile(@PathVariable Long id) {
        MemberDto.Response response = memberService.getProfile(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto.Response> updateProfile(@AuthenticationPrincipal User user,
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid MemberDto.UpdateProfileRequest request) {
        memberService.validateById(id, user.getId());
        MemberDto.Response response = memberService.updateProfile(id, request.username());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/sale")
    public ResponseEntity<Page<PostDto.Response>> getSaleList(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> saleList = postService.getAllPost(pageable, null, id, null, null);

        return ResponseEntity.ok(saleList);
    }

    @GetMapping("/{id}/purchase")
    public ResponseEntity<Page<PostDto.Response>> getPurchaseList(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> purchaseList = postService.getAllPost(pageable, null, null, null, id);

        return ResponseEntity.ok(purchaseList);
    }
}
