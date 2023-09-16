package com.devcourse.be04daangnmarket.image.util;

import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.image.dto.ImageDto;

public class ImageConverter {
    public static ImageDto.ImageResponse toResponse(Image image) {
        return new ImageDto.ImageResponse(
      				image.getName(),
      				image.getPath(),
      				image.getType().getFullType(),
      				image.getSize(),
      				image.getDomainName(),
      				image.getDomainId()
      		);
    }
}
