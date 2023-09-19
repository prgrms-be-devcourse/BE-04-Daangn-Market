package com.devcourse.be04daangnmarket.image.application;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import com.devcourse.be04daangnmarket.common.image.dto.Type;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        ImageDto.ImageDetail imageDetail = new ImageDto.ImageDetail("test1", "uniqueName-test1.png", Type.PNG);
        List<ImageDto.ImageDetail> imageDetails = List.of(imageDetail);

        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(
                "test1",
                Type.PNG,
                "images/uniqueName-test1.png",
                domainName,
                domainId);

        given(imageRepository.save(any())).willReturn(image);

        //when
        List<String> responses = imageService.save(imageDetails, domainName, domainId);

        // then
        assertThat(responses.get(0)).isEqualTo(image.getPath());
        verify(imageRepository, times(1)).save(any());
    }

    @Test
    void 조회_성공() {
        //given
        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(
                "test1",
                Type.PNG,
                "images/uniqueName-test1.png",
                domainName,
                domainId);
        List<Image> imagePaths = List.of(image);

        given(imageRepository.findAllByDomainNameAndDomainId(domainName, domainId))
                .willReturn(imagePaths);

        //when
        List<String> responses = imageService.getImages(domainName, domainId);

        // then
        assertThat(responses.get(0)).isEqualTo(image.getPath());
        verify(imageRepository, times(1)).findAllByDomainNameAndDomainId(domainName, domainId);
    }

    @Test
    void 삭제시_삭제상태변경_확인() {
        //given
        DomainName domainName = DomainName.COMMENT;
        Long domainId = 1L;

        Image image = new Image(
                "test1",
                Type.PNG,
                "images/uniqueName-test1.png",
                domainName,
                domainId);

        List<Image> imagePaths = List.of(image);

        given(imageRepository.findAllByDomainNameAndDomainId(domainName, domainId))
                .willReturn(imagePaths);

        //when
        imageService.deleteAllImages(domainName, domainId);

        // then
        assertThat(image.getStatus()).isEqualTo(Status.DELETED);
        verify(imageRepository, times(1)).findAllByDomainNameAndDomainId(domainName, domainId);
    }
}
