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
    public Slice<Post> getPostsWithMultiFilters(Category category, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> post = query.from(Post.class);

        Predicate categoryPredicate = Optional.ofNullable(category)
                .map(key -> builder.equal(post.get("category"), category))
                .orElse(null);

        Predicate where = builder.and(Stream.of(categoryPredicate).filter(Objects::nonNull).toArray(Predicate[]::new));

        query.select(post).where(where);

        TypedQuery<Post> typedQuery = em.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Post> resultList = typedQuery.getResultList();

        return new SliceImpl<>(resultList, pageable, hasMorePages(resultList.size(), pageable));
    }

    private boolean hasMorePages(int pageSize, Pageable pageable) {
        return pageSize > pageable.getPageSize();
    }
}
