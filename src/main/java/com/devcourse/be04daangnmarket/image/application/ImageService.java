package com.devcourse.be04daangnmarket.image.application;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.repository.ImageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devcourse.be04daangnmarket.image.util.ImageConverter.toEntity;

@Transactional(readOnly = true)
@Service
public class ImageService {
	private static final String RELATIVE_PATH = "images/";

	private final ImageRepository imageRepository;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@Transactional
	public List<String> save(List<ImageDto.ImageDetail> images, DomainName domainName, Long domainId) {
		return images.stream()
               .map(imageDetail -> {
				   String relativePath = getRelativePath(imageDetail.uniqueName());
				   Image image = toEntity(imageDetail.originName(), imageDetail.type(), relativePath, domainName, domainId);
				   imageRepository.save(image);

                   return relativePath;
               })
				.toList();
	}

	private String getRelativePath(String uniqueName) {
		return RELATIVE_PATH + uniqueName;
	}

	public List<String> getImages(DomainName domainName, Long domainId) {
		List<Image> images = getAllImages(domainName, domainId);

		return images.stream()
				.filter(image -> image.getStatus().equals(Status.ALIVE))
				.map(image -> image.getPath())
				.toList();
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
