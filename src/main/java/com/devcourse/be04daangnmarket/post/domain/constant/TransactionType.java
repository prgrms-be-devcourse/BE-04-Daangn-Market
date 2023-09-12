package com.devcourse.be04daangnmarket.post.domain.constant;

public enum TransactionType {
	SALE("판매하기"),
	SHARE("나눔하기"),
	;

	private final String description;

	TransactionType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
