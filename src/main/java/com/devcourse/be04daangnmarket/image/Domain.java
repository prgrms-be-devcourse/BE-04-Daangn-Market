package com.devcourse.be04daangnmarket.image;

public enum Domain {
	POST("post"),
	COMMENT("comment"),
	MEMBER("member")
	;

	private final String description;

	Domain(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}