package com.devcourse.be04daangnmarket.comment.api;

import com.devcourse.be04daangnmarket.comment.application.CommentService;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestPart(name = "request") CreateCommentRequest request,
                                                  @RequestPart(name = "images") List<MultipartFile> files) {
        CommentResponse response = commentService.create(request, files);

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

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "createdAt.desc") String order) {
        String[] sorted = order.split("\\.");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorted[0]).descending());
        if (sorted[1].equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sorted[0]).ascending());
        }

        Page<CommentResponse> response = commentService.getPage(pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestPart(name = "request") UpdateCommentRequest request, @RequestPart(name = "images") List <MultipartFile> files) {
        CommentResponse response = commentService.update(id, request, files);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
