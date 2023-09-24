package com.devcourse.be04daangnmarket.common.aop.lock;

public enum ErrorMessage {
    LOCK_ACQUISITION_FAILURE("락을 획득할 수 없습니다."),
    LOCK_DURING_ACQUISITION_FAILURE("락을 획득하는 동안 문제가 발생했습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}