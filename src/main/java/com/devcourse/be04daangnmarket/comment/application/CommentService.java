package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.comment.util.CommentConverter;
import com.devcourse.be04daangnmarket.member.application.ProfileService;
import com.devcourse.be04daangnmarket.member.domain.Profile;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;
import com.devcourse.be04daangnmarket.member.util.ProfileConverter;
import com.devcourse.be04daangnmarket.post.application.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.devcourse.be04daangnmarket.comment.exception.ErrorMessage.NOT_FOUND_COMMENT;
import static com.devcourse.be04daangnmarket.comment.util.CommentConverter.toResponse;

@Transactional(readOnly = true)
@Service
public class CommentService implements CommentProviderService {
    private static final int START_NUMBER = 0;

    private final ImageService imageService;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final ProfileService profileService;

    public CommentService(ImageService imageService,
                          CommentRepository commentRepository,
                          PostService postService, ProfileService profileService) {
        this.imageService = imageService;
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.profileService = profileService;
    }

    @Transactional
    public CommentDto.CommentResponse create(CommentDto.CreateCommentRequest request, Long userId, String username) {
        Long postId = postService.findPostById(request.postId()).getId();
        Comment comment = CommentConverter.toEntity(request, postId, userId);

        createCommentGroupNumber(comment);

        Comment saved = commentRepository.save(comment);
        List<ImageDto.ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return toResponse(saved, images, username);
    }

    private void createCommentGroupNumber(Comment comment) {
        Integer groupNumber = commentRepository.findMaxCommentGroup().orElse(START_NUMBER);
        comment.addGroup(groupNumber);
    }

    @Transactional
    public CommentDto.CommentResponse createReply(CommentDto.CreateReplyCommentRequest request,
                                                  Long userId,
                                                  String username) {
        Long postId = postService.findPostById(request.postId()).getId();
        Comment comment = CommentConverter.toEntity(request, postId, userId);

        addMaxSequenceToReplyComment(request, comment);

        Comment saved = commentRepository.save(comment);
        List<ImageDto.ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return toResponse(saved, images, username);
    }

    private void addMaxSequenceToReplyComment(CommentDto.CreateReplyCommentRequest request, Comment comment) {
        Integer seqNumber = commentRepository.findMaxSeqFromCommentGroup(request.commentGroup())
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_COMMENT.getMessage()));
        comment.addSeq(seqNumber);
    }

    @Transactional
    public CommentDto.CommentResponse update(Long id, CommentDto.UpdateCommentRequest request, String username) {
        Comment comment = getComment(id);
        comment.update(request.content());

        imageService.deleteAllImages(DomainName.COMMENT, id);
        List<ImageDto.ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, id);

        return toResponse(comment, images, username);
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_COMMENT.getMessage()));
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = getComment(id);

        if (comment.isGroupComment()) {
            List<Comment> sameGroupComments = commentRepository.findAllByCommentGroup(comment.getCommentGroup());

            for (Comment sameGroupComment : sameGroupComments) {
                deleteComment(sameGroupComment);
            }
        }

        deleteComment(comment);
    }

    private void deleteComment(Comment comment) {
        comment.deleteStatus();
        imageService.deleteAllImages(DomainName.COMMENT, comment.getId());
    }

    public CommentDto.CommentResponse getDetail(Long id) {
        Comment comment = getComment(id);
        String username = profileService.get(comment.getMemberId()).getUsername();

        List<ImageDto.ImageResponse> images = imageService.getImages(DomainName.COMMENT, id);

        return toResponse(comment, images, username);
    }

    @Override
    public Page<CommentDto.PostCommentResponse> getPostComments(Long postId, Pageable pageable) {
        List<Comment> postComments = commentRepository.findAllByPostIdToSeqIsZero(postId);
        List<CommentDto.PostCommentResponse> postCommentResponses = new ArrayList<>();

        for (Comment comment : postComments) {
            String commentUsername = profileService.get(comment.getMemberId()).getUsername();
            String postTitle = postService.findPostById(comment.getPostId()).getTitle();
            List<ImageDto.ImageResponse> commentImages = imageService.getImages(DomainName.COMMENT, comment.getId());

            List<CommentDto.CommentResponse> replyCommentResponses = getReplyComments(comment);

            postCommentResponses.add(
                    toResponse(comment, commentUsername, postTitle, commentImages, replyCommentResponses)
            );
        }

        return new PageImpl<>(postCommentResponses, pageable, postCommentResponses.size());
    }

    private List<CommentDto.CommentResponse> getReplyComments(Comment comment) {
        List<Comment> replyComments = commentRepository.findRepliesByCommentGroup(comment.getCommentGroup());
        List<CommentDto.CommentResponse> replyCommentResponses = new ArrayList<>();

        for (Comment reply : replyComments) {
            String replyUsername = profileService.get(comment.getMemberId()).getUsername();
            List<ImageDto.ImageResponse> replyImages = imageService.getImages(DomainName.COMMENT, reply.getId());

            CommentDto.CommentResponse commentResponse = toResponse(reply, replyImages, replyUsername);
            replyCommentResponses.add(commentResponse);
        }

        return replyCommentResponses;
    }

    @Override
    public Page<ProfileDto.Response> getCommenterByPostId(Long writerId, Pageable pageable) {
         return commentRepository.findDistinctMemberIdsByPostIdAndNotInWriterId(writerId, writerId, pageable)
                .map(memberId -> {
                    Profile memberProfile = profileService.get(memberId);

                    return ProfileConverter.toResponse(memberProfile);
                });
    }
}
