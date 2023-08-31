package com.devcourse.be04daangnmarket.comment.exception;

public enum ExceptionMessage {
    INVALID_CONTENT("댓글의 값이 올바르지 않습니다."),
    NOT_FOUND_COMMENT("댓글을 찾지 못했습니다.");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
