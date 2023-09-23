package com.devcourse.be04daangnmarket.member.api;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.member.application.ReviewService;
import com.devcourse.be04daangnmarket.member.dto.ReviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReviewRestController {

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
}
