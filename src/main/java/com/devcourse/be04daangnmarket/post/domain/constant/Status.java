package com.devcourse.be04daangnmarket.post.domain.constant;

public enum Status {
	FOR_SALE("판매중"),
	SOLD("거래완료"),
	HIDDEN("숨김"),
	DELETED("삭제")
	;

	private final String description;

	Status(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
