package com.devcourse.be04daangnmarket.member.exception;

import com.devcourse.be04daangnmarket.common.exception.NotFoundException;

public class NotFoundMemberException extends NotFoundException {
    public NotFoundMemberException(String message) {
        super(message);
    }
}
