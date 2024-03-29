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
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostWithCursorRepositoryImplTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("단일 조회 조건이 있을 때 조건을 반영한 동적 쿼리 페이징 결과 성공")
    void getPostsWithFilterTest() {
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                null,
                null,
                Category.DIGITAL_DEVICES,
                null,
                null,
                null,
                pageable
        );

        // then
        assertEquals(3, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("단일 조회 조건이 없을 때 조건을 반영한 동적 쿼리 페이징 결과 성공")
    void getPostsWithNoneFilterTest() {
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                null,
                null,
                null,
                null,
                null,
                null,
                pageable
        );

        // then
        assertEquals(4, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("다중 조회 조건이 있을 때 조건을 반영한 동적 쿼리 페이징 결과 성공")
    void getPostsWithMultiFilterTest() {
        // given
        List<Post> posts = List.of(
                new Post(2L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(2L, "house~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                null,
                null,
                Category.DIGITAL_DEVICES,
                2L,
                null,
                null,
                pageable
        );

        // then
        assertEquals(2, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("키워드가 포함된 게시글 페이징 조회 성공")
    void getPostsWithKeywordFilterTest() {
        // given
        List<Post> posts = List.of(
                new Post(2L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mousekey~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(2L, "house~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                null,
                null,
                null,
                null,
                null,
                "key",
                pageable
        );

        // then
        assertEquals(3, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("첫 조회시 게시글 아이디 값 없는 커서 기반 페이징 조회 성공")
    void getPostsWithFilterWithCursorFirst() {
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                null,
                null,
                null,
                null,
                null,
                "key",
                pageable
        );

        // then
        assertEquals(3L, selectedPost.getContent().get(0).getId());
        assertEquals(1L, selectedPost.getContent().get(1).getId());
    }

    @Test
    @DisplayName("첫 조회 이후 게시글 아이디 값을 사용한 커서 기반 페이징 조회 성공")
    void getPostsWithFilterWithCursor() {
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price"));
        Slice<Post> selectedPost = postRepository.findPostsWithCursorWithFilters(
                2L,
                null,
                null,
                null,
                null,
                "key",
                pageable
        );

        // then
        assertEquals(1L, selectedPost.getContent().get(0).getId());
        assertEquals(1, selectedPost.getContent().size());
    }

    @Test
    @DisplayName("생성 시간에 대한 커서 기반 페이징 첫 조회 성공")
    void getPostsByCursorWithFirstTest() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(1L, "house~!", "this keyboard is good", 300000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<Post> pageResult = postRepository.findPostsWithCursor(null, null, pageable);

        // then
        assertEquals(4L, pageResult.getContent().get(0).getId());
        assertEquals(2, pageResult.getContent().size());
    }

    @Test
    @DisplayName("생성 시간에 대한 커서 기반 페이징 첫 번째 이후 조회 성공")
    void getPostsByCursorTest() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(1L, "house~!", "this keyboard is good", 300000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        Post selectedPost = postRepository.findById(3L).get();
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<Post> pageResult = postRepository.findPostsWithCursor(3L, selectedPost.getCreatedAt(), pageable);

        // then
        assertEquals(2L, pageResult.getContent().get(0).getId());
        assertEquals(2, pageResult.getContent().size());
    }

    @Test
    @DisplayName("생성 시간에 대한 오름차순 커서 기반 페이징 조회 성공")
    void PostRepositoryCustomImplTest() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "keyboard~!", "this keyboard is good", 100000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "mouse~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES),
                new Post(1L, "keyKey~!", "this keyboard is good", 200000, TransactionType.SALE,
                        Category.HOUSEHOLD_KITCHEN),
                new Post(1L, "house~!", "this keyboard is good", 300000, TransactionType.SALE,
                        Category.DIGITAL_DEVICES)
        );
        postRepository.saveAll(posts);

        // when
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "createdAt"));
        Slice<Post> pageResult = postRepository.findPostsWithCursor(null, null, pageable);

        // then
        assertEquals(1L, pageResult.getContent().get(0).getId());
        assertEquals(2, pageResult.getContent().size());
    }
}
