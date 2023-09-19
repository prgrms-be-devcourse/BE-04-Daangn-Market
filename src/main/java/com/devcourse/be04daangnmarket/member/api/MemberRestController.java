package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;
import jakarta.validation.Valid;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public MemberRestController(MemberService memberService, PostService postService) {
        this.memberService = memberService;
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto.Response> getProfile(@PathVariable Long id) {
        ProfileDto.Response response = memberService.toProfile(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto.Response> updateProfile(@AuthenticationPrincipal User user,
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid ProfileDto.UpdateRequest request) {
        memberService.validateById(id, user.getId());
        ProfileDto.Response response = memberService.updateProfile(id, request.username());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/sale")
    public ResponseEntity<Page<PostDto.Response>> getSaleList(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> saleList = postService.getPostByMemberId(id, pageable);

        return ResponseEntity.ok(saleList);
    }

    @GetMapping("/{id}/purchase")
    public ResponseEntity<Page<PostDto.Response>> getPurchaseList(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> purchaseList = postService.getPostByBuyerId(id, pageable);

        return ResponseEntity.ok(purchaseList);
    }
}
