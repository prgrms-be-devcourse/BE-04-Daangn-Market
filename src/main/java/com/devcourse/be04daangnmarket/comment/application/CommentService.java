package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.comment.util.CommentConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class CommentService {
    private final ImageService imageService;
    private final CommentRepository commentRepository;

    public CommentService(ImageService imageService, CommentRepository commentRepository) {
        this.imageService = imageService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponse create(CreateCommentRequest request, List<MultipartFile> files) {
        Comment comment = CommentConverter.toEntity(request);
        Comment saved = commentRepository.save(comment);
        List<ImageResponse> images = imageService.uploadImages(files, DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, images);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = getOne(id);
        comment.deleteStatus();
        imageService.deleteAllImages(DomainName.COMMENT, id);
    }

    private Comment getOne(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND_COMMENT.getMessage()));
    }

    public CommentResponse getDetail(Long id) {
        Comment comment = getOne(id);
        List<ImageResponse> images = imageService.getImages(DomainName.COMMENT, id);

        return CommentConverter.toResponse(comment, images);
    }

    public Page<CommentResponse> getPage(Pageable pageable) {
        List<Comment> comments = commentRepository.findAll();
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (Comment comment : comments) {
            List<ImageResponse> images = imageService.getImages(DomainName.COMMENT, comment.getId());
            CommentResponse commentResponse = new CommentResponse(comment.getContent(), images);
            commentResponses.add(commentResponse);
        }

        return new PageImpl<>(commentResponses, pageable, commentResponses.size());
    }

    @Transactional
    public CommentResponse update(Long id, UpdateCommentRequest request, List<MultipartFile> files) {
        Comment comment = getOne(id);
        comment.update(request.content());
        imageService.deleteAllImages(DomainName.COMMENT, id);
        List<ImageResponse> images = imageService.uploadImages(files, DomainName.COMMENT, id);

        return CommentConverter.toResponse(comment, images);
    }
}
