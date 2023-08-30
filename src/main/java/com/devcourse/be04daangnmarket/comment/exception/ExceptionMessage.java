package com.devcourse.be04daangnmarket.comment.exception;

public enum ExceptionMessage {
    INVALID_COMMENT("댓글의 길이가 잘못 되었습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
