package com.devcourse.be04daangnmarket.post.domain;

public enum TransactionType {

	SALE("판매하기"),
	SHARE("나눔하기"),
	;

	private final String description;

	private TransactionType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
