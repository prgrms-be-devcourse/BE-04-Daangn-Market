package com.devcourse.be04daangnmarket.member.domain;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Entity
public class Review extends BaseEntity {
    public enum WriterRole {
        SELLER, BUYER
    }

    private Long ownerId;

    private Long postId;

    private Long writerId;

    @Enumerated(EnumType.STRING)
    private WriterRole role;

    private String content;

    @Builder
    public Review(Long ownerId,
                  Long postId,
                  Long writerId,
                  String content,
                  WriterRole writerRole) {
        this.ownerId = ownerId;
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
        this.role = writerRole;
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

    public WriterRole getWriterRole() {
        return role;
    }
}
