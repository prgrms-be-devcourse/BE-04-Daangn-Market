package com.devcourse.be04daangnmarket.image;

import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageCreator {

	@Value("${custom.base-path.image}")
	private String BASE_PATH;

	private static final String UPLOAD_DIRECTORY = "image/";

	public List<Image> createImages(List<MultipartFile> files, Domain domain) throws IOException {
		List<Image> images = new ArrayList<>();

		for (MultipartFile file : files)
			images.add(createImage(file, domain));

		return images;
	}

	public Image createImage(MultipartFile file, Domain domain) throws IOException {
		if (file.isEmpty())
			return null;

		String URL_PATH = UPLOAD_DIRECTORY + domain.getDescription();
		String filePath = BASE_PATH + URL_PATH;

		String fileName = UUID.randomUUID() + StringUtils.cleanPath(file.getOriginalFilename());

		try {
			File directory = new File(filePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File imageFile = new File(directory, fileName);
			file.transferTo(imageFile);
		} catch (IOException e) {
			throw new IOException(FILE_SAVE_ERROR.getMessage());
		}

		return new Image(file.getOriginalFilename(), URL_PATH + "/" + fileName);
	}

}
