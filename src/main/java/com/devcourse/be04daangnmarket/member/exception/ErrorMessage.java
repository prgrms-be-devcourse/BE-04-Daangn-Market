package com.devcourse.be04daangnmarket.member.exception;

public enum ErrorMessage {
    FAIL_LOGIN("이메일 또는 비밀번호를 확인해주세요");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
