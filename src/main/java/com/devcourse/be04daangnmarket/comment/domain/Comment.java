package com.devcourse.be04daangnmarket.comment.domain;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "comments")
@DynamicInsert
public class Comment extends BaseEntity {
    @Column(length = 500, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Long memberId;//작성자 Id

    @Column(nullable = false)
    private Long postId;//댓글이 작성된 게시글 Id

    @Column(nullable = false)
    private int commentGroup;//그룹 Id 댓글을 작성할 때 마다 증가

    @Column(nullable = false)
    private int seq;//댓글의 순서 1번이 댓글 주인, 뒤로 대댓글

    protected Comment() {
    }

    public Comment(String content, Long memberId, Long postId) {
        this.content = content;
        this.memberId = memberId;
        this.postId = postId;
        this.seq = 0;
    }

    public Comment(String content, Long memberId, Long postId, int commentGroup) {
        this.content = content;
        this.memberId = memberId;
        this.postId = postId;
        this.commentGroup = commentGroup;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPostId() {
        return postId;
    }

    public int getCommentGroup() {
        return commentGroup;
    }

    public int getSeq() {
        return seq;
    }

    public void addGroup(int groupNumber) {
        this.commentGroup = groupNumber + 1;
    }

    public void addSeq(int seqNumber) {
        this.seq = seqNumber + 1;
    }

    public void deleteStatus() {
        this.status = Status.DELETED;
    }

    public void update(String content) {
        this.content = content;
    }
}
