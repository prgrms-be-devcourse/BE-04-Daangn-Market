package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.member.application.ReviewService;
import com.devcourse.be04daangnmarket.member.domain.Review.WriterRole;
import com.devcourse.be04daangnmarket.member.dto.ReviewDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReviewRestController {
    private final int PAGE_SIZE = 5;

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/posts/{id}/review")
    public ResponseEntity<ReviewDto.Response> createReview(@AuthenticationPrincipal User user,
                                                           @PathVariable Long id,
                                                           @RequestBody ReviewDto.CreateRequest request) {
        ReviewDto.Response response = reviewService.create(user.getId(), id, request.content());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/members/{id}/review")
    public ResponseEntity<Page<ReviewDto.Response>> getAllByMember(@PathVariable @NotNull Long id,
                                                                   @RequestParam(required = false) WriterRole role,
                                                                   @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<ReviewDto.Response> response = reviewService.getAllByMember(id, role, pageable);

        return ResponseEntity.ok(response);
    }
}
