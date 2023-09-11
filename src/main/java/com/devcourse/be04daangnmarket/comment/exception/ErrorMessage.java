package com.devcourse.be04daangnmarket.comment.exception;

public enum ErrorMessage {
    INVALID_CONTENT("올바르지 않은 댓글입니다."),
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
