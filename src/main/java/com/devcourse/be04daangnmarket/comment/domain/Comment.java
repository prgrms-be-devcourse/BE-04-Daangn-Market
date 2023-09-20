package com.devcourse.be04daangnmarket.comment.domain;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import com.devcourse.be04daangnmarket.member.domain.Member;
import com.devcourse.be04daangnmarket.post.domain.Post;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Objects;

@Entity
@Table(name = "comments")
@DynamicInsert
public class Comment extends BaseEntity {
    @Column(length = 500, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;//작성자 Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;//댓글이 작성된 게시글 Id

    @Column(nullable = false)
    private int commentGroup;//그룹 Id 댓글을 작성할 때 마다 증가

    @Column(nullable = false)
    private int seq;//댓글의 순서 1번이 댓글 주인, 뒤로 대댓글

    protected Comment() {
    }

    public Comment(String content, Member member, Post post) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.seq = 0;
    }

    public Comment(String content, Member member, Post post, int commentGroup) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.commentGroup = commentGroup;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public Member getMember() {
        return member;
    }

    public Post getPost() {
        return post;
    }

    public int getCommentGroup() {
        return commentGroup;
    }

    public int getSeq() {
        return seq;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getPostId() {
        return post.getId();
    }

    public void addPost(Post post) {
        if (Objects.nonNull(this.post)) {
            this.post.getComments().remove(this);
        }

        this.post = post;
        post.getComments().add(this);
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

    public boolean isGroupComment() {
        return this.seq == 0;
    }
}
