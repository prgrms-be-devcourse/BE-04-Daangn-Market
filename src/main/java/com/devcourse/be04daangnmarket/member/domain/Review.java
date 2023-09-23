package com.devcourse.be04daangnmarket.member.domain;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Entity;

@Entity
public class Review extends BaseEntity {

    private Long ownerId;

    private Long postId;

    private Long writerId;

    private String content;

    public Review(Long ownerId, Long postId, Long writerId, String content) {
        this.ownerId = ownerId;
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
    }

    protected Review() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public String getContent() {
        return content;
    }
}
