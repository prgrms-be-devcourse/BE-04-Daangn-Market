package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.dto.CreateReplyCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.PostCommentResponse;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.dto.CommentResponse;
import com.devcourse.be04daangnmarket.comment.dto.CreateCommentRequest;
import com.devcourse.be04daangnmarket.comment.dto.UpdateCommentRequest;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.comment.util.CommentConverter;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.repository.MemberRepository;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage.NOT_FOUND_COMMENT;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.NOT_FOUND_USER;
import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.NOT_FOUND_POST;

@Transactional(readOnly = true)
@Service
public class CommentService {
    private final ImageService imageService;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentService(ImageService imageService, CommentRepository commentRepository, MemberRepository memberRepository, PostRepository postRepository) {
        this.imageService = imageService;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentResponse create(CreateCommentRequest request, Long userId, String username) {
        Comment comment = CommentConverter.toEntity(request, userId);

        Integer groupNumber = commentRepository.findMaxCommentGroup().orElse(0);
        comment.addGroup(groupNumber);

        Comment saved = commentRepository.save(comment);
        List<ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, images, username);
    }

    @Transactional
    public CommentResponse createReply(CreateReplyCommentRequest request, Long userId, String username) {
        Comment comment = CommentConverter.toEntity(request, userId);

        Integer seqNumber = commentRepository.findMaxSeqFromCommentGroup(request.commentGroup())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_COMMENT.getMessage()));
        comment.addSeq(seqNumber);

        Comment saved = commentRepository.save(comment);
        List<ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, images, username);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = getOne(id);

        if (isGroupComment(comment)) {
            List<Comment> sameGroupComments = commentRepository.findAllByCommentGroup(comment.getCommentGroup());

            for (Comment sameGroupComment : sameGroupComments) {
                sameGroupComment.deleteStatus();
                imageService.deleteAllImages(DomainName.COMMENT, sameGroupComment.getId());
            }
        }

        comment.deleteStatus();
        imageService.deleteAllImages(DomainName.COMMENT, id);
    }

    private boolean isGroupComment(Comment comment) {
        return comment.getSeq() == 0;
    }

    private Comment getOne(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT.getMessage()));
    }

    public CommentResponse getDetail(Long id) {
        Comment comment = getOne(id);
        String username = getMember(comment.getMemberId()).getUsername();
        List<ImageResponse> images = imageService.getImages(DomainName.COMMENT, id);

        return CommentConverter.toResponse(comment, images, username);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
    }

    public Page<PostCommentResponse> getPostComment(Long postId, Pageable pageable) {
        List<Comment> postComments = commentRepository.findAllByPostIdToSeqIsZero(postId);
        List<PostCommentResponse> postCommentResponses = new ArrayList<>();

        for (Comment comment : postComments) {
            String commentUsername = getMember(comment.getMemberId()).getUsername();
            String postName = getPost(comment.getPostId()).getTitle();
            List<ImageResponse> commentImages = imageService.getImages(DomainName.COMMENT, comment.getId());

            List<Comment> replies = commentRepository.findRepliesByCommentGroup(comment.getCommentGroup());

            List<CommentResponse> replyCommentResponses = replies.stream().map(reply -> {
                String replyUsername = getMember(reply.getMemberId()).getUsername();
                List<ImageResponse> replyImages = imageService.getImages(DomainName.COMMENT, reply.getId());

                return new CommentResponse(reply.getId(), reply.getMemberId(), replyUsername, reply.getPostId(),
                        reply.getContent(), replyImages, reply.getCreatedAt(), reply.getUpdatedAt());
            }).collect(Collectors.toList());

            postCommentResponses.add(new PostCommentResponse(comment.getId(), comment.getMemberId(), commentUsername, comment.getPostId(), postName, comment.getContent(),
                                commentImages, replyCommentResponses, comment.getCreatedAt(), comment.getUpdatedAt()));
        }

        return new PageImpl<>(postCommentResponses, pageable, postCommentResponses.size());
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_POST.getMessage()));
    }

    @Transactional
    public CommentResponse update(Long id, UpdateCommentRequest request, String username) {
        Comment comment = getOne(id);
        comment.update(request.content());
        List<ImageResponse> images = imageService.getImages(DomainName.COMMENT, comment.getId());
        
        if (isExistImages(request.files())) {
            imageService.deleteAllImages(DomainName.COMMENT, id);
            images = imageService.uploadImages(request.files(), DomainName.COMMENT, id);
        }

        return CommentConverter.toResponse(comment, images, username);
    }

    private boolean isExistImages(List<MultipartFile> files) {
        return files != null;
    }
}
