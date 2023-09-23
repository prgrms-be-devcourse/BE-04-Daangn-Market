package com.devcourse.be04daangnmarket.member.util;

import com.devcourse.be04daangnmarket.member.domain.Review;
import com.devcourse.be04daangnmarket.member.dto.ReviewDto;

public class ReviewConverter {
    public static ReviewDto.Response toResponse(Review review) {
        return new ReviewDto.Response(
                review.getId(),
                review.getOwnerId(),
                review.getPostId(),
                review.getWriterId(),
                review.getContent()
        );
    }
}
