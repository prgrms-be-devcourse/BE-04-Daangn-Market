package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Review;
import com.devcourse.be04daangnmarket.member.dto.ReviewDto;
import com.devcourse.be04daangnmarket.member.repository.ReviewRepository;
import com.devcourse.be04daangnmarket.member.util.ReviewConverter;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.ILLEGAL_USER_ACCESS;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PostService postService;

    public ReviewService(ReviewRepository reviewRepository, PostService postService) {
        this.reviewRepository = reviewRepository;
        this.postService = postService;
    }

    public ReviewDto.Response create(Long authUserId, Long postId, String content) {
        Post post = postService.findPostById(postId);

        Review review = getOne(authUserId, post, content);
        Review savedReview = reviewRepository.save(review);

        return ReviewConverter.toResponse(savedReview);
    }

    private Review getOne(Long authUserId, Post post, String content) {
        Long sellerId = post.getMemberId();
        Long buyerId = post.getBuyerId();

        validateMember(authUserId, sellerId, buyerId);

        if (isEquals(authUserId, buyerId)) {
            return new Review(sellerId, post.getId(), authUserId, content);
        }

        return new Review(buyerId, post.getId(), authUserId, content);
    }

    private void validateMember(Long authUserId, Long sellerId, Long buyerId) {
        if (isNotEquals(authUserId, sellerId) && isNotEquals(authUserId, buyerId)) {
            throw new IllegalArgumentException(ILLEGAL_USER_ACCESS.getMessage());
        }
    }

    private boolean isEquals(Long authUserId, Long memberId) {
        return authUserId.equals(memberId);
    }

    private boolean isNotEquals(Long authUserId, Long memberId) {
        return !isEquals(authUserId, memberId);
    }
}
