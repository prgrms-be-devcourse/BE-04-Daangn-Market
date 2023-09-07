package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.CreateReplyCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.PostCommentResponse;
import com.devcourse.be04daangnmarket.common.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
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

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/reply")
    public ResponseEntity<CommentResponse> createReply(CreateReplyCommentRequest request, @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.createReply(request, user.getId(), user.getUsername());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.delete(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getDetail(@PathVariable("id") Long id) {
        CommentResponse response = commentService.getDetail(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/page/{postId}")
    public ResponseEntity<Page<PostCommentResponse>> getPage(@PathVariable("postId") Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt.desc") String order) {
        String[] sorted = order.split("\\.");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorted[0]).descending());

        if (sorted[1].equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sorted[0]).ascending());
        }

        Page<PostCommentResponse> response = commentService.getPostComment(postId, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, UpdateCommentRequest request, @AuthenticationPrincipal User user) {
        CommentResponse response = commentService.update(id, request, user.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
