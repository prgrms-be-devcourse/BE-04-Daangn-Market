package com.devcourse.be04daangnmarket.image.util;

import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.common.image.dto.Type;

public class ImageConverter {
    public static Image toEntity(String name, Type type, String path, DomainName domainName, Long domainId) {
        return new Image(name, type, path, domainName, domainId);
    }
}
