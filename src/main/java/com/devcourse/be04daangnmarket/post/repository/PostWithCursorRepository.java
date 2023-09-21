package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface PostWithCursorRepository {

    public Slice<Post> findPostsWithCursorWithFilters(Long id,
                                                    LocalDateTime createdAt,
                                                    Category category,
                                                    Long memberId,
                                                    Long buyerId,
                                                    String keyword,
                                                    Pageable pageable);

    Slice<Post> findPostsWithCursor(Long id,
                                   LocalDateTime createdAt,
                                   Pageable pageable);
}
