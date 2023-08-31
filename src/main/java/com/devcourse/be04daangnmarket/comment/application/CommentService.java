package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.comment.util.CommentConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponse create(CreateCommentRequest request) {
        Comment comment = CommentConverter.toEntity(request);
        Comment saved = commentRepository.save(comment);

        return CommentConverter.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = getOne(id);
        comment.deleteStatus();
    }

    private Comment getOne(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND_COMMENT.getMessage()));
    }

    public CommentResponse getOneComment(Long id) {
        Comment comment = getOne(id);

        return CommentConverter.toResponse(comment);
    }
}
