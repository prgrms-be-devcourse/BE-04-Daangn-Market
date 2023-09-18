package com.devcourse.be04daangnmarket.common.image;

import com.devcourse.be04daangnmarket.image.domain.constant.Type;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_UPLOAD_EXCEPTION;

@Component
public class LocalImageUpload implements ImageUpload {
    @Value("${custom.base-path.image}")
   	private String FOLDER_PATH;

	@Override
    public List<ImageDto.ImageDetail> uploadImages(List<MultipartFile> multipartFiles) {
		if (isEmptyImages(multipartFiles)) {
			return Collections.emptyList();
		}

		return multipartFiles.stream()
				.map(this::uploadImage)
				.toList();
    }

	private boolean isEmptyImages(List<MultipartFile> multipartFiles) {
		return multipartFiles == null || multipartFiles.get(0).isEmpty();
	}


    private ImageDto.ImageDetail uploadImage(MultipartFile multipartFile) {
		Type imageType = Type.findImageType(multipartFile.getContentType());
        String fileNameWithoutSpaces = multipartFile.getOriginalFilename().replaceAll(" ", "");
        String uniqueName = createUniqueName(fileNameWithoutSpaces);

        saveImageToLocalStorage(multipartFile, uniqueName);

		return new ImageDto.ImageDetail(multipartFile.getOriginalFilename(), uniqueName, imageType);
    }

    private String createUniqueName(String originalName) {
   		return UUID.randomUUID() + "-" + StringUtils.cleanPath(originalName);
   	}

    private void saveImageToLocalStorage(MultipartFile multipartFile, String uniqueName) {
   		try {
   			java.io.File file = new java.io.File(FOLDER_PATH);

   			if (isEmptyFile(file)) {
   				file.mkdirs();
   			}
   			multipartFile.transferTo(new java.io.File(getFullPath(uniqueName)));
   		} catch (IOException e) {
   			throw new FileUploadException(FILE_UPLOAD_EXCEPTION.getMessage());
   		}
   	}

    private boolean isEmptyFile(java.io.File file) {
   		return !file.exists();
   	}

    private String getFullPath(String uniqueName) {
   		return FOLDER_PATH + uniqueName;
   	}
}
