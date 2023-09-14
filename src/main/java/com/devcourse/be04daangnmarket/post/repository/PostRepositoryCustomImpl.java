package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

        Predicate predicate1 = Optional.ofNullable(id)
                .map(key -> builder.lessThan(post.get("id"), key))
                .orElse(null);

        Predicate predicate2 = Optional.ofNullable(category)
                .map(key -> builder.equal(post.get("category"), key))
                .orElse(null);

        Predicate predicate3 = Optional.ofNullable(memberId)
                .map(key -> builder.equal(post.get("memberId"), key))
                .orElse(null);

        Predicate predicate4 = Optional.ofNullable(buyerId)
                .map(key -> builder.equal(post.get("buyerId"), key))
                .orElse(null);

        Predicate predicate5 = Optional.ofNullable(keyword)
                .map(key -> builder.like(post.get("title"), "%" + key + "%"))
                .orElse(null);



        Predicate where = builder.and(Stream.of(predicate1, predicate2, predicate3, predicate4, predicate5)
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new));

        Order order = builder.desc(post.get("id"));

        query.select(post).where(where).orderBy(order);

        TypedQuery<Post> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(pageable.getPageSize() + 1);
        List<Post> resultList = typedQuery.getResultList();

        boolean hasMorePages = resultList.size() > pageable.getPageSize();

        if (hasMorePages) {
            resultList.remove(resultList.size() - 1);
        }

        return new SliceImpl<>(resultList, pageable, hasMorePages);
    }
}
