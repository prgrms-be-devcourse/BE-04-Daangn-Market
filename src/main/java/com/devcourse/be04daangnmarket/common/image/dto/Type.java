package com.devcourse.be04daangnmarket.common.image.dto;

import com.devcourse.be04daangnmarket.image.exception.ExceptionMessage;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;

import java.util.Arrays;

public enum Type {
    JPEG("image/jpeg"),
    PNG("image/png"),
    JPG("image/jpg")
    ;

    private final String fullType;

    Type(String fullType) {
        this.fullType = fullType;
    }

    public String getFullType() {
        return fullType;
    }

    public static Type findImageType(String typeName) {
        return Arrays.stream(Type.values())
                .filter(type -> type.getFullType().equals(typeName))
                .findFirst()
                .orElseThrow(
                        () -> new FileUploadException(ExceptionMessage.NOT_CORRECT_IMAGE_TYPE_EXCEPTION.getMessage())
                );
    }
}
