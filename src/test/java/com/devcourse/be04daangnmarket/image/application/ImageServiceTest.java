package com.devcourse.be04daangnmarket.image.application;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.image.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Test
    void 저장_성공() {
        //given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile(
                "구직_공고문",
                "구직_공고문.png",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes());
        multipartFiles.add(imageFile);

        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                imageFile.getSize(),
                "images/randomPath-" + imageFile.getOriginalFilename(),
                domainName,
                domainId);

        given(imageRepository.save(any())).willReturn(image);

        //when
        List<ImageResponse> responses = imageService.uploadImages(multipartFiles, domainName, domainId);

        // then
        assertThat(responses.get(0).name()).isEqualTo(image.getName());
        assertThat(responses.get(0).type()).isEqualTo(image.getType());
        verify(imageRepository, times(1)).save(any());
    }

    @Test
    void 조회_성공() {
        //given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile("구직_공고문", "구직_공고문.png", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        multipartFiles.add(imageFile);

        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                imageFile.getSize(),
                "images/randomPath-" + imageFile.getOriginalFilename(),
                domainName,
                domainId);
        List<Image> images = List.of(image);

        given(imageRepository.findAllByDomainNameAndDomainId(domainName, domainId))
                .willReturn(images);

        //when
        List<ImageResponse> responses = imageService.getImages(domainName, domainId);

        // then
        assertThat(responses.get(0).name()).isEqualTo(image.getName());
        assertThat(responses.get(0).type()).isEqualTo(image.getType());
        verify(imageRepository, times(1)).findAllByDomainNameAndDomainId(domainName, domainId);
    }

    @Test
    void 삭제시_삭제상태변경_확인() {
        //given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MockMultipartFile imageFile = new MockMultipartFile(
                "구직_공고문",
                "구직_공고문.png",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes());
        multipartFiles.add(imageFile);

        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(imageFile.getOriginalFilename(), imageFile.getContentType(),
                imageFile.getSize(), "images/randomPath-" + imageFile.getOriginalFilename(), domainName, domainId);
        List<Image> images = List.of(image);

        given(imageRepository.findAllByDomainNameAndDomainId(domainName, domainId))
                .willReturn(images);

        //when
        imageService.deleteAllImages(domainName, domainId);

        // then
        assertThat(image.getStatus()).isEqualTo(Status.DELETED);
        verify(imageRepository, times(1)).findAllByDomainNameAndDomainId(domainName, domainId);
    }
}
