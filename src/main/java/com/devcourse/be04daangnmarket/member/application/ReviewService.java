package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Review;
import com.devcourse.be04daangnmarket.member.domain.Review.WriterRole;
import com.devcourse.be04daangnmarket.member.dto.ReviewDto;
import com.devcourse.be04daangnmarket.member.repository.ReviewRepository;
import com.devcourse.be04daangnmarket.member.util.ReviewConverter;
import com.devcourse.be04daangnmarket.member.util.ReviewSpecification;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devcourse.be04daangnmarket.member.domain.Review.WriterRole.BUYER;
import static com.devcourse.be04daangnmarket.member.domain.Review.WriterRole.SELLER;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.DUPLICATED_REVIEW;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.ILLEGAL_USER_ACCESS;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.NOT_COMPLETED_TRANSACTION;

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
        checkDuplication(authUserId, postId);

        Post post = postService.findPostById(postId);
        Review review = getOne(authUserId, post, content);
        Review savedReview = reviewRepository.save(review);

        return ReviewConverter.toResponse(savedReview);
    }

    public Page<ReviewDto.Response> getAllByMember(Long ownerId, WriterRole role, Pageable pageable) {
        Specification<Review> specification = ReviewSpecification.findWith(ownerId, role);

        return reviewRepository.findAll(specification, pageable)
                .map(ReviewConverter::toResponse);
    }

    private Review getOne(Long authUserId, Post post, String content) {
        Long sellerId = post.getMemberId();
        Long buyerId = post.getBuyerId();

        validateMember(authUserId, sellerId, buyerId);

        if (isEquals(authUserId, buyerId)) {
            return Review.builder()
                    .ownerId(sellerId)
                    .postId(post.getId())
                    .writerId(authUserId)
                    .writerRole(BUYER)
                    .content(content)
                    .build();
        }

        return Review.builder()
                .ownerId(buyerId)
                .postId(post.getId())
                .writerId(authUserId)
                .writerRole(SELLER)
                .content(content)
                .build();
    }

    private void checkDuplication(Long writerId, Long postId) {
        if (reviewRepository.findByWriterIdAndPostId(writerId, postId).isPresent()) {
            throw new IllegalArgumentException(DUPLICATED_REVIEW.getMessage());
        }
    }

    private void validateMember(Long authUserId, Long sellerId, Long buyerId) {
        if (buyerId == null) {
            throw new IllegalArgumentException(NOT_COMPLETED_TRANSACTION.getMessage());
        }

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
