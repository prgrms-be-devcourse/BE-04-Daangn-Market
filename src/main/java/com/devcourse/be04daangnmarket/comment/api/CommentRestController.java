package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.CreateReplyCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.PostCommentResponse;
import com.devcourse.be04daangnmarket.common.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(CreateCommentRequest request, @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.create(request, user.getId(), user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/reply")
    public ResponseEntity<CommentResponse> createReply(CreateReplyCommentRequest request, @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.createReply(request, user.getId(), user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getDetail(@PathVariable Long id) {
        CommentResponse response = commentService.getDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<PostCommentResponse>> getPostComments(@PathVariable Long postId,
                                                                     @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostCommentResponse> response = commentService.getPostComments(postId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, UpdateCommentRequest request, @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.update(id, request, user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
