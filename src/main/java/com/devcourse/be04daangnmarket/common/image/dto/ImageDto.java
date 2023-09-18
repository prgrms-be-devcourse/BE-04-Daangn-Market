package com.devcourse.be04daangnmarket.common.image.dto;

import com.devcourse.be04daangnmarket.image.domain.constant.Type;

public class ImageDto {
    public record ImageDetail(
            String originName,

            String uniqueName,

            Type type
    ) {
    }
}
