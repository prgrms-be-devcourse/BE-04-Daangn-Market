package com.devcourse.be04daangnmarket.post.exception;

import com.devcourse.be04daangnmarket.common.exception.NotFoundException;

public class NotFoundPostException extends NotFoundException {
    public NotFoundPostException(String message) {
        super(message);
    }
}
