package com.devcourse.be04daangnmarket.comment.exception;

public enum ErrorMessage {
    NOT_FOUND_COMMENT("댓글이 존재하지 않습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
