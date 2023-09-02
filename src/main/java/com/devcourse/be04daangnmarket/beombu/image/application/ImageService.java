package com.devcourse.be04daangnmarket.beombu.image.application;

import com.devcourse.be04daangnmarket.beombu.image.domain.DomainName;
import com.devcourse.be04daangnmarket.beombu.image.domain.Image;
import com.devcourse.be04daangnmarket.beombu.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.beombu.image.exception.FileDeleteException;
import com.devcourse.be04daangnmarket.beombu.image.exception.FileUploadException;
import com.devcourse.be04daangnmarket.beombu.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.devcourse.be04daangnmarket.beombu.image.exception.ExceptionMessage.FILE_DELETE_EXCEPTION;
import static com.devcourse.be04daangnmarket.beombu.image.exception.ExceptionMessage.FILE_UPLOAD_EXCEPTION;

@Transactional(readOnly = true)
@Service
public class ImageService {
    private final String FOLDER_PATH = "C:\\Users\\User\\Desktop\\image\\";

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public List<ImageResponse> uploadImages(List<MultipartFile> multipartFiles, DomainName domainName, Long domainId) {
        List<ImageResponse> responses = new ArrayList<>();

        if (isExistImages(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                String originalName = multipartFile.getOriginalFilename();
                String uniqueName = createUniqueName(originalName);

                saveImageToLocalStorage(multipartFile, uniqueName);

                Image image = new Image(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), getFullPath(uniqueName), domainName, domainId);
                imageRepository.save(image);
                responses.add(toDto(image));
            }
        }

        return responses;
    }

    private boolean isExistImages(List<MultipartFile> multipartFiles) {
        return !multipartFiles.isEmpty();
    }

    private String createUniqueName(String originalName) {
        return UUID.randomUUID().toString() + "-" + originalName;
    }

    private void saveImageToLocalStorage(MultipartFile multipartFile, String uniqueName) {
        try {
            File file = new File(FOLDER_PATH);

            if (isExistFile(file)) {
                file.mkdirs();
            }
            multipartFile.transferTo(new File(getFullPath(uniqueName)));
        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_EXCEPTION.getMessage());
        }
    }

    private static boolean isExistFile(File file) {
        return !file.exists();
    }

    private String getFullPath(String uniqueName) {
        return FOLDER_PATH + uniqueName;
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

        entities.stream()
                .map(Image::getPath)
                .forEach(this::deleteImageToLocalStorage);

        imageRepository.deleteAllByDomainNameAndDomainId(domainName, domainId);
    }

    private List<Image> getAllImageEntities(DomainName domainName, Long domainId) {
        return imageRepository.findAllByDomainNameAndDomainId(domainName, domainId);
    }

    private void deleteImageToLocalStorage(String path){
        Path fullPath = Paths.get(path);

        try {
            Files.delete(fullPath);
        } catch (IOException e) {
            throw new FileDeleteException(FILE_DELETE_EXCEPTION.getMessage());
        }
    }
}
