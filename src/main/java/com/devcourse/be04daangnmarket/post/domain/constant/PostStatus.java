package com.devcourse.be04daangnmarket.post.domain.constant;

public enum PostStatus {
    FOR_SALE("판매중"),
    SOLD("거래완료"),
    HIDDEN("숨김"),
    ;

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
