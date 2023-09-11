package com.devcourse.be04daangnmarket.member.exception;

public class DuplicatedUsernameException extends IllegalArgumentException {
    public DuplicatedUsernameException(String message) {
        super(message);
    }
}
