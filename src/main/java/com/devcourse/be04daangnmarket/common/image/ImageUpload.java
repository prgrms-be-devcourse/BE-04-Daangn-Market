package com.devcourse.be04daangnmarket.common.image;

import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUpload {
    List<ImageDto.ImageDetail> uploadImages(List<MultipartFile> multipartFiles);
}
