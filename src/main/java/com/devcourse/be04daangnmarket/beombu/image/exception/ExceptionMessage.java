package com.devcourse.be04daangnmarket.beombu.image.exception;

public enum ExceptionMessage {
    FILE_DELETE_EXCEPTION("파일 삭제에 실패하였습니다."),
    FILE_UPLOAD_EXCEPTION("파일 등록에 실패하였습니다.");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
