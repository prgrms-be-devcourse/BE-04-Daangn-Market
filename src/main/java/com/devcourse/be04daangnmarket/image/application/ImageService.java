package com.devcourse.be04daangnmarket.image.application;

import com.devcourse.be04daangnmarket.image.domain.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.image.exception.FileDeleteException;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;
import com.devcourse.be04daangnmarket.image.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_DELETE_EXCEPTION;
import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_UPLOAD_EXCEPTION;

@Transactional(readOnly = true)
@Service
public class ImageService {
	private static final String RELATIVE_PATH = "images/";

	@Value("${custom.base-path.image}")
	private String FOLDER_PATH;

	private final ImageRepository imageRepository;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@Transactional
	public List<ImageResponse> uploadImages(List<MultipartFile> multipartFiles, DomainName domainName, Long domainId) {
		List<ImageResponse> imageResponses = new ArrayList<>();

		if (isExistImages(multipartFiles)) {
			for (MultipartFile multipartFile : multipartFiles) {
				if(multipartFile.isEmpty()) continue;

				String originalName = multipartFile.getOriginalFilename();
				String uniqueName = createUniqueName(originalName);

				saveImageToLocalStorage(multipartFile, uniqueName);

				Image image = new Image(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
					multipartFile.getSize(), getRelativePath(uniqueName), domainName, domainId);

				imageRepository.save(image);
				imageResponses.add(toDto(image));
			}
		}

		return imageResponses;
	}

	private boolean isExistImages(List<MultipartFile> multipartFiles) {
		return !multipartFiles.isEmpty();
	}

	private String createUniqueName(String originalName) {
		return UUID.randomUUID() + "-" + StringUtils.cleanPath(originalName);

	}

	private void saveImageToLocalStorage(MultipartFile multipartFile, String uniqueName) {
		try {
			java.io.File file = new java.io.File(FOLDER_PATH + RELATIVE_PATH);

			if (isEmptyFile(file)) {
				file.mkdirs();
			}
			multipartFile.transferTo(new java.io.File(getFullPath(uniqueName)));
		} catch (IOException e) {
			throw new FileUploadException(FILE_UPLOAD_EXCEPTION.getMessage());
		}
	}

	private static boolean isEmptyFile(java.io.File file) {
		return !file.exists();
	}

	private String getRelativePath(String uniqueName) {
		return RELATIVE_PATH + uniqueName;
	}

	private String getFullPath(String uniqueName) {
		return FOLDER_PATH + RELATIVE_PATH + uniqueName;
	}

	public List<ImageResponse> getImages(DomainName domainName, Long domainId) {
		List<Image> images = imageRepository.findAllByDomainNameAndDomainId(domainName, domainId);

		return images.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	private ImageResponse toDto(Image image) {
		return new ImageResponse(
			image.getName(),
			image.getPath(),
			image.getType(),
			image.getSize(),
			image.getDomainName(),
			image.getDomainId());
	}

	@Transactional
	public void deleteAllImages(DomainName domainName, Long domainId) {
		List<Image> entities = getAllImageEntities(domainName, domainId);

		if (entities.isEmpty()) {
			return;
		}

		for (Image entry : entities) {
			entry.deleteImage();
		}
	}

	private List<Image> getAllImageEntities(DomainName domainName, Long domainId) {
		return imageRepository.findAllByDomainNameAndDomainId(domainName, domainId);
	}

	private void deleteImageToDatabase(DomainName domainName, Long domainId) {
		imageRepository.deleteAllByDomainNameAndDomainId(domainName, domainId);
	}

	private void deleteImageToLocalStorage(String path) {
		Path fullPath = Paths.get(FOLDER_PATH + path);

		try {
			Files.delete(fullPath);
		} catch (IOException e) {
			throw new FileDeleteException(FILE_DELETE_EXCEPTION.getMessage());
		}
	}
}
