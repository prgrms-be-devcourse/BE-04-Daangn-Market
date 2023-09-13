package com.devcourse.be04daangnmarket.post.repository;

import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryCustomImplTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("조회 조건이 있을 때 조건을 반영한 동적 쿼리 페이징 결과 성공")
    void getPostsWithMultiFiltersTest() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(1L, "house~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Slice<Post> selectedPost = postRepository.getPostsWithMultiFilters(Category.DIGITAL_DEVICES, pageable);

        // then
        assertEquals(3, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("조회 조건이 없을 때 조건을 반영한 동적 쿼리 페이징 결과 성공")
    void getPostsWithMultiFiltersTest2() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(1L, "house~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Slice<Post> selectedPost = postRepository.getPostsWithMultiFilters(null, pageable);

        // then
        assertEquals(4, selectedPost.getContent().size());
    }
}