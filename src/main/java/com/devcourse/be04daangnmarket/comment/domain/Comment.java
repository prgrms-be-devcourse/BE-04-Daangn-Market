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
    @Column(name = "comment", length = 500)
    private String comment;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ALIVE'")
    private DeletedStatus isDeleted;

    public Comment() {

    }

    public Comment(String comment) {
        validateComment(comment);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public DeletedStatus getIsDeleted() {
        return isDeleted;
    }

    private void validateComment(String comment) {
        if (isCommentWithinRange(comment)) {
            throw new IllegalArgumentException(INVALID_COMMENT.getMessage());
        }
    }

    private static boolean isCommentWithinRange(String comment) {
        return comment.length() > 500;
    }
}
