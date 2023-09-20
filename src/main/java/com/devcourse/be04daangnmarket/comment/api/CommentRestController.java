package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.common.auth.User;
import jakarta.validation.Valid;
import com.devcourse.be04daangnmarket.common.image.LocalImageUpload;
import com.devcourse.be04daangnmarket.common.image.ImageUpload;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {
    private final CommentService commentService;
    private final ImageUpload imageUpload;

    public CommentRestController(CommentService commentService, LocalImageUpload uploadImageService) {
        this.commentService = commentService;
        this.imageUpload = uploadImageService;
    }

    @PostMapping
    public ResponseEntity<CommentDto.CommentResponse> create(@Valid CommentDto.CreateCommentRequest request,
                                                             @AuthenticationPrincipal User user) {
        List<ImageDto.ImageDetail> imageDetails = imageUpload.uploadImages(request.files());
        CommentDto.CommentResponse response = commentService.create(request.postId(),
                user.getId(),
                user.getUsername(),
                request.content(),
                imageDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/reply")
    public ResponseEntity<CommentDto.CommentResponse> createReply(@Valid CommentDto.CreateReplyCommentRequest request,
                                                                  @AuthenticationPrincipal User user) {
        List<ImageDto.ImageDetail> imageDetails = imageUpload.uploadImages(request.files());
        CommentDto.CommentResponse response = commentService.createReply(request.postId(),
                user.getId(),
                user.getUsername(),
                request.commentGroup(),
                request.content(),
                imageDetails);

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
        List<ImageDto.ImageDetail> imageDetails = imageUpload.uploadImages(request.files());
        CommentDto.CommentResponse response = commentService.update(id,
                request.postId(),
                user.getUsername(),
                request.content(),
                imageDetails);

        return ResponseEntity.ok(response);
    }
}
