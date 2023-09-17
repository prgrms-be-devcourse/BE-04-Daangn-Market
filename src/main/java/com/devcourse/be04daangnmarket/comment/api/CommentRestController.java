package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import jakarta.validation.Valid;
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
    public ResponseEntity<CommentDto.CommentResponse> create(@Valid CommentDto.CreateCommentRequest request,
                                                             @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.create(request, user.getId(), user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/reply")
    public ResponseEntity<CommentDto.CommentResponse> createReply(@Valid CommentDto.CreateReplyCommentRequest request,
                                                                  @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.createReply(request, user.getId(), user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto.CommentResponse> getDetail(@PathVariable Long id) {
        CommentDto.CommentResponse response = commentService.getDetail(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto.CommentResponse> update(@PathVariable Long id,
                                                             @Valid CommentDto.UpdateCommentRequest request,
                                                             @AuthenticationPrincipal User user) {
        CommentDto.CommentResponse response = commentService.update(id, request, user.getUsername());

        return ResponseEntity.ok(response);
    }
}
