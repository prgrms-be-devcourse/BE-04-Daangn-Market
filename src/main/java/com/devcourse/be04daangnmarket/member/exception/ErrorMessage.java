package com.devcourse.be04daangnmarket.member.exception;

public enum ErrorMessage {
    FAIL_LOGIN("이메일 또는 비밀번호를 확인해주세요"),
    DUPLICATED_USERNAME("이미 존재하는 닉네임입니다."),
    NOT_FOUND_USER("존재하지 않는 유저입니다."),
    ILLEGAL_USER_ACCESS("잘못된 유저의 접근입니다."),
    NOT_FOUND_PROFILE("프로필을 찾을 수 없습니다."),
    DUPLICATED_REVIEW("해당 리뷰가 이미 존재합니다."),
    NOT_COMPLETED_TRANSACTION("거래가 완료되지 않았습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
