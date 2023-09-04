package com.devcourse.be04daangnmarket.post.exception;

public enum ErrorMessage {

	NOT_FOUND_POST("존재하지 않는 게시물 입니다."),
	FILE_SAVE_ERROR("파일을 저장할 수 없습니다.")
	;

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
