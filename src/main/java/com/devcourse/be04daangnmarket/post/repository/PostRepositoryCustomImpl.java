package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final EntityManager em;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Slice<Post> getPostsWithMultiFilters(Long id,
                                                Category category,
                                                Long memberId,
                                                Long buyerId,
                                                String keyword,
                                                Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> post = query.from(Post.class);

        Predicate idCondition = Optional.ofNullable(id)
                .map(key -> builder.lessThan(post.get("id"), key))
                .orElse(null);

        Predicate categoryCondition = Optional.ofNullable(category)
                .map(key -> builder.equal(post.get("category"), key))
                .orElse(null);

        Predicate memberIdCondition = Optional.ofNullable(memberId)
                .map(key -> builder.equal(post.get("memberId"), key))
                .orElse(null);

        Predicate buyerIdCondition = Optional.ofNullable(buyerId)
                .map(key -> builder.equal(post.get("buyerId"), key))
                .orElse(null);

        Predicate keywordCondition = Optional.ofNullable(keyword)
                .map(key -> builder.like(post.get("title"), "%" + key + "%"))
                .orElse(null);

        Predicate where = builder.and(Stream.of(
                        idCondition,
                        categoryCondition,
                        memberIdCondition,
                        buyerIdCondition,
                        keywordCondition
                )
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new));

        Order order = builder.desc(post.get("id"));

        query.select(post)
                .where(where)
                .orderBy(order);

        TypedQuery<Post> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(pageable.getPageSize() + 1);
        List<Post> resultList = typedQuery.getResultList();

        return toSlice(resultList, pageable);
    }

    @Override
    public Slice<Post> getPostsWithCursor(Long id,
                                          LocalDateTime createdAt,
                                          Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> post = query.from(Post.class);

        Sort createdAtSort = pageable.getSortOr(Sort.by(Sort.Direction.DESC, "createdAt"));

        Predicate cursorRestriction = Optional.ofNullable(createdAt)
                .map(key -> {
                    Predicate where = switch (createdAtSort.getOrderFor("createdAt").getDirection()) {
                        case DESC -> {
                            Predicate createdAtLt = builder.lessThan(post.get("createdAt"), key);

                            Predicate createdAtEq = builder.equal(post.get("createdAt"), key);
                            Predicate idLt = builder.lessThan(post.get("id"), id);

                            Predicate and = builder.and(createdAtEq, idLt);
                            Predicate or = builder.or(createdAtLt, and);

                            yield or;
                        }
                        case ASC -> {
                            Predicate createdAtGt = builder.greaterThan(post.get("createdAt"), key);

                            Predicate createdAtEq = builder.equal(post.get("createdAt"), key);
                            Predicate idLt = builder.lessThan(post.get("id"), id);

                            Predicate and = builder.and(createdAtEq, idLt);
                            Predicate or = builder.or(createdAtGt, and);

                            yield or;
                        }
                    };

                    return where;
                })
                .orElseGet(() -> null);

        Predicate[] where = Stream.of(cursorRestriction).filter(Objects::nonNull).toArray(Predicate[]::new);

        Order createdAtOrder = createdAtSort.getOrderFor("createdAt").isDescending() ? builder.desc(post.get("createdAt")) : builder.asc(post.get("createdAt"));
        Order idOrder = builder.desc(post.get("id"));

        query.select(post)
                .where(where)
                .orderBy(createdAtOrder, idOrder);

        TypedQuery<Post> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(pageable.getPageSize() + 1);
        List<Post> resultList = typedQuery.getResultList();

        return toSlice(resultList, pageable);
    }

    private SliceImpl<Post> toSlice(List<Post> posts, Pageable pageable) {
        if (posts.size() > pageable.getPageSize()) {
            posts.remove(posts.size() - 1);

            return new SliceImpl<>(posts, pageable, true);
        }

        return new SliceImpl<>(posts, pageable, false);
    }
}
