package com.devcourse.be04daangnmarket.member.repository;

import com.devcourse.be04daangnmarket.member.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAll(Specification<Review> specification, Pageable pageable);

    Optional<Review> findByWriterIdAndPostId(Long writerId, Long postId);
}
