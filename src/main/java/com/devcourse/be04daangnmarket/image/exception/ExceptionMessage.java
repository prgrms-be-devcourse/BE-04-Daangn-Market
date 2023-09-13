package com.devcourse.be04daangnmarket.image.exception;

public enum ExceptionMessage {
    FILE_DELETE_EXCEPTION("파일 삭제에 실패하였습니다."),
    FILE_UPLOAD_EXCEPTION("파일 등록에 실패하였습니다."),
    INVALID_IMAGE_TYPE_EXCEPTION("이미지 타입이 옳지 않습니다.")
    ;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
