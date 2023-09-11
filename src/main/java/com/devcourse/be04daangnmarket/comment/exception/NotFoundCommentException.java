package com.devcourse.be04daangnmarket.comment.exception;

import com.devcourse.be04daangnmarket.common.exception.NotFoundException;

public class NotFoundCommentException extends NotFoundException {
    public NotFoundCommentException(String message) {
        super(message);
    }
}
