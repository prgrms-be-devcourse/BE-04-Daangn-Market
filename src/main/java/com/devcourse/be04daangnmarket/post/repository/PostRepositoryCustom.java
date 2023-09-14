package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<Post> getPostsWithMultiFilters(Long id,
                                         Category category,
                                         Long memberId,
                                         Long buyerId,
                                         String keyword,
                                         Pageable pageable
    );
}
