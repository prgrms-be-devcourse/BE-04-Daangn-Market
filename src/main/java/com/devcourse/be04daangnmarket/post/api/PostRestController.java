package com.devcourse.be04daangnmarket.post.api;

import com.devcourse.be04daangnmarket.comment.application.CommentProviderService;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.common.image.ImageUpload;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;

import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "post", description = "게시글 API")
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

    @Tag(name = "post")
    @Operation(description = "[토큰 필요] 유저가 게시글을 생성한다", responses = {
            @ApiResponse(responseCode = "201", description = "성공적으로 게시글을 생성한 경우"),
            @ApiResponse(responseCode = "500", description = "토큰을 넣지 않은 경우")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @Tag(name = "post")
    @Operation(description = "유저가 단일 게시글을 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글을 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글을 조회한 경우")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostDto.Response> getPost(@PathVariable @NotNull Long id,
                                                    HttpServletRequest req,
                                                    HttpServletResponse res) {
        PostDto.Response response = postService.getPost(id, req, res);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "유저가 게시글을 전체 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글을 조회한 경우")
    })
    @GetMapping
    public ResponseEntity<Page<PostDto.Response>> getAllPost(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> responses = postService.getAllPost(pageable);

        return ResponseEntity.ok(responses);
    }

    @Tag(name = "post")
    @Operation(description = "유저가 다중 필터를 적용해 게시글을 전체 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 다중 필터를 적용해 게시글을 전체 조회한 경우")
    })
    @GetMapping("/filter")
    public ResponseEntity<Slice<PostDto.Response>> getPostsWithCursorWithFilters(PostDto.FilterRequest request,
                                                                                 @PageableDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<PostDto.Response> responses = postService.getPostsWithCursorWithFilters(request, pageable);

        return ResponseEntity.ok(responses);
    }

    @Tag(name = "post")
    @Operation(description = "유저가 카테고리별 게시글을 전체 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 카테고리별 게시글을 조회한 경우")
    })
    @GetMapping("/category")
    public ResponseEntity<Page<PostDto.Response>> getPostByCategory(@RequestParam @NotNull Category category,
                                                                    @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByCategory(category, pageable);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "유저는 판매자가 작성한 게시글을 전체 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 판매자가 작성한 게시글을 전체 조회한 경우")
    })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<PostDto.Response>> getPostByMemberId(@PathVariable @NotNull Long memberId,
                                                                    @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByMemberId(memberId, pageable);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "유저가 키워드로 게시글을 검색해서 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글을 조회한 경우")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<PostDto.Response>> getPostByKeyword(@RequestParam @NotBlank String keyword,
                                                                   @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
        Page<PostDto.Response> response = postService.getPostByKeyword(keyword, pageable);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "[토큰 필요] 유저가 게시글을 수정한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글을 수정한 경우"),
            @ApiResponse(responseCode = "500", description = "토큰을 넣지 않은 경우")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @Tag(name = "post")
    @Operation(description = "유저가 게시글 상태를 수정한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글 상태를 수정한 경우")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PostDto.Response> updatePostStatus(@PathVariable @NotNull Long id,
                                                             @RequestBody @Valid PostDto.StatusUpdateRequest request) {
        PostDto.Response response = postService.updatePostStatus(id, request.postStatus());

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "판매자가 게시글 상품 구매자가 결정한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글 상품 구매자가 결정된 경우")
    })
    @PatchMapping("/{id}/purchase")
    public ResponseEntity<PostDto.Response> purchaseProduct(@PathVariable @NotNull Long id,
                                                            @RequestBody @Valid PostDto.BuyerUpdateRequest request) {
        PostDto.Response response = postService.purchaseProduct(id, request.buyerId());

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "유저가 게시글을 삭제한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글을 삭제하는 경우")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Tag(name = "post")
    @Operation(description = "유저가 게시글의 댓글을 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글의 댓글을 조회하는 경우")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDto.PostCommentResponse>> getPostComments(@PathVariable Long id,
                                                                                @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<CommentDto.PostCommentResponse> response = commentService.getPostComments(id, pageable);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "post")
    @Operation(description = "판매자가 게시글에 대한 댓글 작성자를 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글에 대한 댓글 작성자를 조회한 경우")
    })
    @GetMapping("/{id}/communicationMembers")
    public ResponseEntity<Page<ProfileDto.Response>> getCommunicationMembers(@PathVariable Long id,
                                                                             @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Long writerId = postService.findPostById(id).getMemberId();
        Page<ProfileDto.Response> responses = commentService.getCommenterByPostId(id, writerId, pageable);

        return ResponseEntity.ok(responses);
    }
}
