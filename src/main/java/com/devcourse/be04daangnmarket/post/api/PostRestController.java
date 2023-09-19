package com.devcourse.be04daangnmarket.post.api;

import com.devcourse.be04daangnmarket.comment.application.CommentProviderService;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.image.ImageUpload;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
public class PostRestController {
    private final int PAGE_SIZE = 5;

    private final PostService postService;
    private final CommentProviderService commentService;
    private final ImageUpload imageUpload;

    public PostRestController(PostService postService, CommentProviderService commentService, ImageUpload imageUpload) {
        this.postService = postService;
        this.commentService = commentService;
        this.imageUpload = imageUpload;
    }

    @PostMapping
    public ResponseEntity<PostDto.Response> createPost(@Valid PostDto.CreateRequest request,
                                                       @AuthenticationPrincipal User user) throws IOException {
        List<ImageDto.ImageDetail> imageDetails = imageUpload.uploadImages(request.files());

        PostDto.Response response = postService.create(
                user.getId(),
                request.title(),
                request.description(),
                request.price(),
                request.transactionType(),
                request.category(),
                imageDetails
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto.Response> getPost(@PathVariable @NotNull Long id,
                                                    HttpServletRequest req,
                                                    HttpServletResponse res) {
        PostDto.Response response = postService.getPost(id, req, res);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostDto.Response>> getAllPost(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> responses = postService.getAllPost(pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/filter")
    public ResponseEntity<Slice<PostDto.Response>> getPostsWithFilters(PostDto.FilterRequest request,
                                                                       Pageable pageable) {
        Slice<PostDto.Response> responses = postService.getPostsWithFilter(request, pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<PostDto.Response>> getPostByCategory(@RequestParam @NotNull Category category,
                                                                    @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByCategory(category, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<PostDto.Response>> getPostByMemberId(@PathVariable @NotNull Long memberId,
                                                                    @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByMemberId(memberId, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostDto.Response>> getPostByKeyword(@RequestParam @NotBlank String keyword,
                                                                   @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByKeyword(keyword, pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto.Response> updatePost(@PathVariable @NotNull Long id,
                                                       @Valid PostDto.UpdateRequest request) {
        List<ImageDto.ImageDetail> imageDetails = imageUpload.uploadImages(request.files());
        PostDto.Response response = postService.update(
                id,
                request.title(),
                request.description(),
                request.price(),
                request.transactionType(),
                request.category(),
                imageDetails
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PostDto.Response> updatePostStatus(@PathVariable @NotNull Long id,
                                                             @RequestBody @Valid PostDto.StatusUpdateRequest request) {
        PostDto.Response response = postService.updatePostStatus(id, request.postStatus());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/purchase")
    public ResponseEntity<PostDto.Response> purchaseProduct(@PathVariable @NotNull Long id,
                                                            @RequestBody @Valid PostDto.BuyerUpdateRequest request) {
        PostDto.Response response = postService.purchaseProduct(id, request.buyerId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDto.PostCommentResponse>> getPostComments(@PathVariable Long id,
                                                                                @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<CommentDto.PostCommentResponse> response = commentService.getPostComments(id, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/communicationMembers")
    public ResponseEntity<Page<MemberDto.Response>> getCommunicationMembers(@PathVariable Long id,
                                                                            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Long writerId = postService.findPostById(id).getMemberId();
        Page<MemberDto.Response> responses = commentService.getCommenterByPostId(writerId, pageable);

        return ResponseEntity.ok(responses);
    }
}
