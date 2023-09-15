package com.devcourse.be04daangnmarket.image.application;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.image.domain.constant.Type;
import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;
import com.devcourse.be04daangnmarket.image.repository.ImageRepository;

import com.devcourse.be04daangnmarket.image.util.ImageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.devcourse.be04daangnmarket.image.exception.ExceptionMessage.FILE_UPLOAD_EXCEPTION;

@Transactional(readOnly = true)
@Service
public class ImageService {
	private static final String RELATIVE_PATH = "images/";

	private final ImageRepository imageRepository;

	@Value("${custom.base-path.image}")
	private String FOLDER_PATH;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@Transactional
	public List<ImageDto.ImageResponse> uploadImages(List<MultipartFile> multipartFiles,
													 DomainName domainName,
													 Long domainId) {
		List<ImageDto.ImageResponse> imageResponses = new ArrayList<>();

		if (isEmptyImages(multipartFiles)) {
			return imageResponses;
		}

		for (MultipartFile multipartFile : multipartFiles) {
			Type imageType = Type.findImageType(multipartFile.getContentType());
			String fileNameWithoutSpaces = multipartFile.getOriginalFilename().replaceAll(" ", "");
			String uniqueName = createUniqueName(fileNameWithoutSpaces);

			saveImageToLocalStorage(multipartFile, uniqueName);

			Image image = new Image(multipartFile.getOriginalFilename(), imageType,
					multipartFile.getSize(), getRelativePath(uniqueName), domainName, domainId);

			imageRepository.save(image);
			imageResponses.add(ImageConverter.toResponse(image));
		}

		return imageResponses;
	}

	private boolean isEmptyImages(List<MultipartFile> multipartFiles) {
		return multipartFiles == null || multipartFiles.get(0).isEmpty();
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

	private String getRelativePath(String uniqueName) {
		return RELATIVE_PATH + uniqueName;
	}

	private String getFullPath(String uniqueName) {
		return FOLDER_PATH + uniqueName;
	}

	public List<ImageDto.ImageResponse> getImages(DomainName domainName, Long domainId) {
		List<Image> images = getAllImages(domainName, domainId);

		return images.stream()
				.filter(image -> image.getStatus().equals(Status.ALIVE))
				.map(ImageConverter::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteAllImages(DomainName domainName, Long domainId) {
		List<Image> images = getAllImages(domainName, domainId);

		if (images.isEmpty()) {
			return;
		}

		for (Image image : images) {
			image.changeStatus();
		}
	}

	private List<Image> getAllImages(DomainName domainName, Long domainId) {
		return imageRepository.findAllByDomainNameAndDomainId(domainName, domainId);
	}
}
