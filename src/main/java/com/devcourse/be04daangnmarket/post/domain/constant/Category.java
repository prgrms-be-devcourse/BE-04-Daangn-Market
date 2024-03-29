package com.devcourse.be04daangnmarket.post.domain.constant;

public enum Category {
	DIGITAL_DEVICES("디지털 기기"),
	HOME_APPLIANCES("가전 제품"),
	MEN_CLOTHING("남성 의류"),
	WOMEN_CLOTHING("여성 의류"),
	HOUSEHOLD_KITCHEN("주방용 가정 용품"),
	HOBBIES_GAMES_GENERAL("취미 및 게임 일반"),
	FURNITURE_INTERIOR("가구 및 인테리어")
	;

	private final String description;

	Category(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
