package com.devcourse.be04daangnmarket.comment.domain;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import static com.devcourse.be04daangnmarket.comment.exception.ExceptionMessage.*;

@Entity
@Table(name = "comments")
@DynamicInsert
public class Comment extends BaseEntity {
    @Column(name = "content", length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ALIVE'")
    private DeletedStatus deletedStatus;

    protected Comment() {

    }

    public Comment(String content) {
        validateContent(content);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public DeletedStatus getDeletedStatus() {
        return deletedStatus;
    }

    private void validateContent(String content) {
        if (isCommentWithinRange(content)) {
            throw new IllegalArgumentException(INVALID_CONTENT.getMessage());
        }
    }

    private boolean isCommentWithinRange(String content) {
        return content.length() > 500;
    }

    public void deleteStatus() {
        this.deletedStatus = DeletedStatus.DELETED;
    }

    public void update(String content) {
        validateContent(content);
        this.content = content;
    }
}
