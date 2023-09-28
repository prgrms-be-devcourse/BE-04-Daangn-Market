package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.devcourse.be04daangnmarket.common.image.LocalImageIOService;
import com.devcourse.be04daangnmarket.common.image.ImageIOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "comment", description = "댓글 API")
@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {
    private final CommentService commentService;
    private final ImageIOService imageIOService;

    public CommentRestController(CommentService commentService, LocalImageIOService uploadImageService) {
        this.commentService = commentService;
        this.imageIOService = uploadImageService;
    }

    @Tag(name = "comment")
    @Operation(description = "[토큰 필요] 유저가 댓글을 생성한다", responses = {
            @ApiResponse(responseCode = "201", description = "성공적으로 댓글을 작성한 경우"),
            @ApiResponse(responseCode = "500", description = "토큰을 넣지 않은 경우")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDto.CommentResponse> create(@Valid CommentDto.CreateCommentRequest request,
                                                             @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.create(request.postId(),
                user.getId(),
                user.getUsername(),
                request.content(),
                request.files());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Tag(name = "comment")
    @Operation(description = "[토큰 필요] 유저가 대댓글을 생성한다", responses = {
            @ApiResponse(responseCode = "201", description = "성공적으로 대댓글을 작성한 경우"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "토큰을 넣지 않은 경우")
    })
    @PostMapping(value = "/reply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDto.CommentResponse> createReply(@Valid CommentDto.CreateReplyCommentRequest request,
                                                                  @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.createReply(request.postId(),
                user.getId(),
                user.getUsername(),
                request.commentGroup(),
                request.content(),
                request.files());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Tag(name = "comment")
    @Operation(description = "유저가 댓글을 삭제한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 댓글을 삭제한 경우"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않습니다.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Tag(name = "comment")
    @Operation(description = "유저가 단일 댓글을 조회한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 단일 댓글을 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto.CommentResponse> getDetail(@PathVariable Long id) {
        CommentDto.CommentResponse response = commentService.getDetail(id);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "comment")
    @Operation(description = "[토큰 필요] 유저가 댓글을 수정한다", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 댓글을 수정한 경우"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "토큰을 넣지 않은 경우")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDto.CommentResponse> update(@PathVariable Long id,
                                                             @Valid CommentDto.UpdateCommentRequest request,
                                                             @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.update(id,
                request.postId(),
                user.getUsername(),
                request.content(),
                request.files());

        return ResponseEntity.ok(response);
    }
}
