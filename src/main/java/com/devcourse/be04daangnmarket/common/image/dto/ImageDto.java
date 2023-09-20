package com.devcourse.be04daangnmarket.common.image.dto;

public class ImageDto {
    public record ImageDetail(
            String originName,

            String uniqueName,

            Type type
    ) {
    }
}
