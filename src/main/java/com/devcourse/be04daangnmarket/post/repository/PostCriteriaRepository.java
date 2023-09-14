package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class PostCriteriaRepository {
    private final EntityManager entityManager;

    public PostCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Slice<Post> findAllByOffsetPaging(Pageable pageable,
                                            Long id,
                                            Category category,
                                            Long memberId,
                                            String title,
                                            Long buyerId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cq = builder.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);

        Predicate ltId = Optional.ofNullable(id)
                .map(value -> builder.lessThan(root.get("id"), value))
                .orElse(null);
        Predicate eqCategory = Optional.ofNullable(category)
                .map(value -> builder.equal(root.get("category"), value))
                .orElse(null);
        Predicate eqMemberId = Optional.ofNullable(memberId)
                .map(value -> builder.equal(root.get("memberId"), value))
                .orElse(null);
        Predicate likeTitle = Optional.ofNullable(title)
                .map(value -> builder.like(root.get("title"), "%" + value + "%"))
                .orElse(null);
        Predicate eqBuyerId = Optional.ofNullable(buyerId)
                .map(value -> builder.equal(root.get("buyerId"), value))
                .orElse(null);

        Predicate predicate = builder.and(
                Stream.of(ltId, eqCategory, eqMemberId, likeTitle, eqBuyerId)
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new)
        );

        cq.select(root)
                .where(predicate)
                .orderBy(builder.desc(root.get("id")));

        TypedQuery<Post> query = entityManager.createQuery(cq);
        List<Post> posts = query.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize() + 1)
                .getResultList();

        return toSlice(pageable, posts);
    }

    private SliceImpl<Post> toSlice(Pageable pageable, List<Post> posts) {
        if (posts.size() > pageable.getPageSize()) {
            posts.remove(posts.size() - 1);
            return new SliceImpl<>(posts, pageable, true);
        }

        return new SliceImpl<>(posts, pageable, false);
    }
}
