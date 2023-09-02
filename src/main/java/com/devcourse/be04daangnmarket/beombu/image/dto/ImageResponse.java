package com.devcourse.be04daangnmarket.beombu.image.dto;

import com.devcourse.be04daangnmarket.beombu.image.domain.DomainName;

public record ImageResponse(
    String name,
    String path,
    String type,
    long size,
    DomainName domainName,
    Long domainId
) {
}
