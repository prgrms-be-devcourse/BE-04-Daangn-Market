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
    public Slice<Post> getPostsWithCursorWithFilers(Long id,
                                                    LocalDateTime createdAt,
                                                    Category category,
                                                    Long memberId,
                                                    Long buyerId,
                                                    String keyword,
                                                    Pageable pageable) {

        Sort createdAtSort = pageable.getSortOr(Sort.by(Sort.Direction.DESC, "createdAt"));

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> post = query.from(Post.class);

        List<Predicate> conditions = new ArrayList<>();
        Optional.ofNullable(id)
                .ifPresent(key -> conditions.add(builder.lessThan(post.get("id"), key)));
        Optional.ofNullable(category)
                .ifPresent(key -> conditions.add(builder.equal(post.get("category"), key)));
        Optional.ofNullable(memberId)
                .ifPresent(key -> conditions.add(builder.equal(post.get("memberId"), key)));
        Optional.ofNullable(buyerId)
                .ifPresent(key -> conditions.add(builder.equal(post.get("buyerId"), key)));
        Optional.ofNullable(keyword)
                .ifPresent(key -> conditions.add(builder.like(post.get("title"), "%" + key + "%")));
        Optional.ofNullable(createdAt)
                .map(key -> {
                    Predicate createdAtEq = builder.equal(post.get("createdAt"), key);
                    Predicate idLt = builder.lessThan(post.get("id"), id);

                    Predicate and = builder.and(createdAtEq, idLt);

                    return createdAtSort.getOrderFor("createdAt").isDescending()
                            ? builder.or(builder.lessThan(post.get("createdAt"), key), and)
                            : builder.or(builder.greaterThan(post.get("createdAt"), key), and);
                }).ifPresent(cursorCondition -> conditions.add(cursorCondition));

        Predicate where = builder.and(conditions.toArray(Predicate[]::new));

        Order createdAtOrder = createdAtSort.getOrderFor("createdAt").isDescending()
                ? builder.desc(post.get("createdAt"))
                : builder.asc(post.get("createdAt"));

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
                    Predicate createdAtEq = builder.equal(post.get("createdAt"), key);
                    Predicate idLt = builder.lessThan(post.get("id"), id);

                    Predicate and = builder.and(createdAtEq, idLt);

                    return createdAtSort.getOrderFor("createdAt").isDescending()
                            ? builder.or(builder.lessThan(post.get("createdAt"), key), and)
                            : builder.or(builder.greaterThan(post.get("createdAt"), key), and);
                })
                .orElse(null);

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
