package com.devcourse.be04daangnmarket.member.util;

import com.devcourse.be04daangnmarket.member.domain.Review;
import com.devcourse.be04daangnmarket.member.domain.Review.WriterRole;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification {
    public static Specification<Review> findWith(final Long ownerId, final WriterRole role) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ownerId != null) {
                predicates.add(builder.equal(root.get("ownerId"), ownerId));
            }

            if (role != null) {
                predicates.add(builder.equal(root.get("role"), role));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
