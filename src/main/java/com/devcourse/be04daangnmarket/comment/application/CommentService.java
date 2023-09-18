package com.devcourse.be04daangnmarket.comment.application;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public CommentDto.CommentResponse create(Long postId,
                                             Long userId,
                                             String username,
                                             String content,
                                             List<ImageDto.ImageDetail> files) {
        Comment comment = CommentConverter.toEntity(content, userId, postId);

        Integer groupNumber = commentRepository.findMaxCommentGroup().orElse(START_NUMBER);
        comment.addGroup(groupNumber);

        Comment saved = commentRepository.save(comment);
        List<String> imagePaths = imageService.save(files, DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, imagePaths, username);
    }

    @Transactional
    public CommentDto.CommentResponse createReply(Long postId,
                                                  Long userId,
                                                  String username,
                                                  int commentGroup,
                                                  String content,
                                                  List<ImageDto.ImageDetail> files) {
        Comment comment = CommentConverter.toEntity(postId, content, commentGroup, userId);

        Integer seqNumber = commentRepository.findMaxSeqFromCommentGroup(commentGroup)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_COMMENT.getMessage()));
        comment.addSeq(seqNumber);

        Comment saved = commentRepository.save(comment);
        List<String> imagePaths = imageService.save(files, DomainName.COMMENT, saved.getId());

        return CommentConverter.toResponse(saved, imagePaths, username);
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
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_COMMENT.getMessage()));
    }

    private boolean isGroupComment(Comment comment) {
        return comment.getSeq() == START_NUMBER;
    }

    public CommentDto.CommentResponse getDetail(Long id) {
        Comment comment = getComment(id);

        String username = memberService.getMember(comment.getMemberId()).getUsername();
        List<String> imagePaths = imageService.getImages(DomainName.COMMENT, id);

        return CommentConverter.toResponse(comment, imagePaths, username);
    }

    public Page<CommentDto.PostCommentResponse> getPostComments(Long postId, Pageable pageable) {
        List<Comment> postComments = commentRepository.findAllByPostIdToSeqIsZero(postId);
        List<CommentDto.PostCommentResponse> postCommentResponses = new ArrayList<>();

        for (Comment comment : postComments) {
            String commentUsername = memberService.getMember(comment.getMemberId()).getUsername();
            String postTitle = postService.findPostById(comment.getPostId()).getTitle();
            List<String> imagePaths = imageService.getImages(DomainName.COMMENT, comment.getId());

            List<CommentDto.CommentResponse> replyCommentResponses = getReplyComments(comment);

            postCommentResponses.add(
                    new CommentDto.PostCommentResponse(
                            comment.getId(),
                            comment.getMemberId(),
                            commentUsername,
                            comment.getPostId(),
                            postTitle,
                            comment.getContent(),
                            imagePaths,
                            replyCommentResponses,
                            comment.getCommentGroup(),
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
            List<String> imagePaths = imageService.getImages(DomainName.COMMENT, reply.getId());

            CommentDto.CommentResponse commentResponse = CommentConverter.toResponse(reply, imagePaths, replyUsername);
            replyCommentResponses.add(commentResponse);
        }

        return replyCommentResponses;
    }

    @Transactional
    public CommentDto.CommentResponse update(Long id,
                                             Long postId,
                                             String username,
                                             String content,
                                             List<ImageDto.ImageDetail> files) {
        Comment comment = getComment(id);
        comment.update(content);

        List<String> imagePaths = imageService.getImages(DomainName.COMMENT, comment.getId());

        if (isExistImages(files)) {
            imageService.deleteAllImages(DomainName.COMMENT, id);
            imagePaths = imageService.save(files, DomainName.COMMENT, id);
        }

        return CommentConverter.toResponse(comment, imagePaths, username);
    }

    private boolean isExistImages(List<ImageDto.ImageDetail> files) {
        return !files.isEmpty();
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
