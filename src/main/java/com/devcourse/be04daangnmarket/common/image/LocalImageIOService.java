package com.devcourse.be04daangnmarket.common.image;

import com.devcourse.be04daangnmarket.common.image.dto.Type;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.exception.FileDeleteException;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_DELETE_EXCEPTION;
import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_UPLOAD_EXCEPTION;

@Component
public class LocalImageIOService implements ImageIOService {
    @Value("${custom.base-path.image}")
   	private String FOLDER_PATH;

	@PostConstruct
	private void makeDirectory() {
		File file = new File(FOLDER_PATH);

		if (isEmptyFile(file)) {
			file.mkdirs();
		}
	}

	@Override
    public List<ImageDto.ImageDetail> uploadImages(List<MultipartFile> multipartFiles) {
		return isEmptyImages(multipartFiles)
				? Collections.emptyList()
				: multipartFiles.stream()
								.map(this::uploadImage)
								.toList();
    }

	private boolean isEmptyImages(List<MultipartFile> multipartFiles) {
		return multipartFiles == null || multipartFiles.get(0).isEmpty();
	}

    private ImageDto.ImageDetail uploadImage(MultipartFile multipartFile) {
		Type imageType = Type.findImageType(multipartFile.getContentType());
        String uniqueName = createUniqueName(imageType);

        saveImageToLocalStorage(multipartFile, uniqueName);

		return new ImageDto.ImageDetail(multipartFile.getOriginalFilename(), uniqueName, imageType);
    }

    private String createUniqueName(Type imageType) {
   		return UUID.randomUUID() + "." + imageType.name();
   	}

    private void saveImageToLocalStorage(MultipartFile multipartFile, String uniqueName) {
   		try {
			String fullPath = getFullPath(uniqueName);
			File file = new File(fullPath);
			multipartFile.transferTo(file);
   		} catch (IOException e) {
   			throw new FileUploadException(FILE_UPLOAD_EXCEPTION.getMessage());
   		}
   	}

    private boolean isEmptyFile(File file) {
   		return !file.exists();
   	}

    private String getFullPath(String uniqueName) {
   		return FOLDER_PATH + "/" + uniqueName;
   	}

	@Override
	public void delete(String path) {
		Path fullPath = Paths.get(FOLDER_PATH + path);

		try {
			Files.delete(fullPath);
		} catch (IOException e) {
			throw new FileDeleteException(FILE_DELETE_EXCEPTION.getMessage());
		}
	}
}
