package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.comment.exception.NotFoundCommentException;
import com.devcourse.be04daangnmarket.comment.repository.CommentRepository;
import com.devcourse.be04daangnmarket.comment.util.CommentConverter;
import com.devcourse.be04daangnmarket.member.application.MemberService;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.member.dto.MemberDto;
import com.devcourse.be04daangnmarket.post.application.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.devcourse.be04daangnmarket.comment.exception.ErrorMessage.NOT_FOUND_COMMENT;

@Transactional(readOnly = true)
@Service
public class CommentService {
    private static final int START_NUMBER = 0;

    private final ImageService imageService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostService postService;

    public CommentService(ImageService imageService,
                          CommentRepository commentRepository,
                          MemberService memberService,
                          PostService postService) {
        this.imageService = imageService;
        this.commentRepository = commentRepository;
        this.memberService = memberService;
        this.postService = postService;
    }

    @Transactional
    public CommentDto.CommentResponse create(CommentDto.CreateCommentRequest request, Long userId, String username) {
        Comment comment = CommentConverter.toEntity(request, userId);

        Integer groupNumber = commentRepository.findMaxCommentGroup().orElse(START_NUMBER);
        comment.addGroup(groupNumber);

        Comment saved = commentRepository.save(comment);
        List<ImageDto.ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, images, username);
    }

    @Transactional
    public CommentDto.CommentResponse createReply(CommentDto.CreateReplyCommentRequest request,
                                                  Long userId,
                                                  String username) {
        Comment comment = CommentConverter.toEntity(request, userId);

        Integer seqNumber = commentRepository.findMaxSeqFromCommentGroup(request.commentGroup())
                .orElseThrow(() -> new NotFoundCommentException(NOT_FOUND_COMMENT.getMessage()));
        comment.addSeq(seqNumber);

        Comment saved = commentRepository.save(comment);
        List<ImageDto.ImageResponse> images = imageService.uploadImages(request.files(), DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, images, username);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = getComment(id);

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

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundCommentException(NOT_FOUND_COMMENT.getMessage()));
    }

    private boolean isGroupComment(Comment comment) {
        return comment.getSeq() == START_NUMBER;
    }

    public CommentDto.CommentResponse getDetail(Long id) {
        Comment comment = getComment(id);

        String username = memberService.getMember(comment.getMemberId()).getUsername();
        List<ImageDto.ImageResponse> images = imageService.getImages(DomainName.COMMENT, id);

        return CommentConverter.toResponse(comment, images, username);
    }

    public Page<CommentDto.PostCommentResponse> getPostComments(Long postId, Pageable pageable) {
        List<Comment> postComments = commentRepository.findAllByPostIdToSeqIsZero(postId);
        List<CommentDto.PostCommentResponse> postCommentResponses = new ArrayList<>();

        for (Comment comment : postComments) {
            String commentUsername = memberService.getMember(comment.getMemberId()).getUsername();
            String postTitle = postService.findPostById(comment.getPostId()).getTitle();
            List<ImageDto.ImageResponse> commentImages = imageService.getImages(DomainName.COMMENT, comment.getId());

            List<CommentDto.CommentResponse> replyCommentResponses = getReplyComments(comment);

            postCommentResponses.add(
                    new CommentDto.PostCommentResponse(
                            comment.getId(),
                            comment.getMemberId(),
                            commentUsername,
                            comment.getPostId(),
                            postTitle,
                            comment.getContent(),
                            commentImages,
                            replyCommentResponses,
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    )
            );
        }

        return new PageImpl<>(postCommentResponses, pageable, postCommentResponses.size());
    }

    private List<CommentDto.CommentResponse> getReplyComments(Comment comment) {
        List<Comment> replyComments = commentRepository.findRepliesByCommentGroup(comment.getCommentGroup());
        List<CommentDto.CommentResponse> replyCommentResponses = new ArrayList<>();

        for (Comment reply : replyComments) {
            String replyUsername = memberService.getMember(comment.getMemberId()).getUsername();
            List<ImageDto.ImageResponse> replyImages = imageService.getImages(DomainName.COMMENT, reply.getId());

            CommentDto.CommentResponse commentResponse = CommentConverter.toResponse(reply, replyImages, replyUsername);
            replyCommentResponses.add(commentResponse);
        }

        return replyCommentResponses;
    }

    @Transactional
    public CommentDto.CommentResponse update(Long id, CommentDto.UpdateCommentRequest request, String username) {
        Comment comment = getComment(id);
        comment.update(request.content());

        List<ImageDto.ImageResponse> images = imageService.getImages(DomainName.COMMENT, comment.getId());

        if (isExistImages(request.files())) {
            imageService.deleteAllImages(DomainName.COMMENT, id);
            images = imageService.uploadImages(request.files(), DomainName.COMMENT, id);
        }

        return CommentConverter.toResponse(comment, images, username);
    }

    private boolean isExistImages(List<MultipartFile> files) {
        return !files.get(START_NUMBER).isEmpty();
    }

    public Page<MemberDto.Response> getCommenterByPostId(Long postId, Pageable pageable) {
        Long writerId = postService.findPostById(postId).getMemberId();

         return commentRepository.findDistinctMemberIdsByPostIdAndNotInWriterId(postId, writerId, pageable)
                .map(memberId -> {
                    Member member = memberService.getMember(memberId);

                    return new MemberDto.Response(
                            member.getId(),
                            member.getUsername(),
                            member.getPhoneNumber(),
                            member.getEmail(),
                            member.getTemperature(),
                            member.getStatus(),
                            member.getCreatedAt(),
                            member.getUpdatedAt()
                    );
                });
    }
}
