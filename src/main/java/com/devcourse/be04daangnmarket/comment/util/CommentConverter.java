package com.devcourse.be04daangnmarket.comment.util;

import com.devcourse.be04daangnmarket.comment.dto.CommentDto;
import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.post.domain.Post;

import java.util.List;

public class CommentConverter {
    public static Comment toEntity(String content, Member member, Post post) {
        return new Comment(content, member, post);
    }

    public static Comment toEntity(Post post, String content, int commentGroup, Member member) {
        return new Comment(content, member, post, commentGroup);
    }

    public static CommentDto.CommentResponse toResponse(Comment comment, List<String> imagePaths, String username) {
        return new CommentDto.CommentResponse(
                comment.getId(),
                comment.getMemberId(),
                username,
                comment.getPostId(),
                comment.getContent(),
                imagePaths,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static CommentDto.PostCommentResponse toResponse(Comment comment, String commentUsername, String postTitle, List<String> imagePaths, List<CommentDto.CommentResponse> replyCommentResponses) {
        return new CommentDto.PostCommentResponse(
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
        );
    }
}
