package com.swyp6.familytravel.image.service;

import com.swyp6.familytravel.common.util.ImageSaveUtil;
import com.swyp6.familytravel.image.entity.Image;
import com.swyp6.familytravel.image.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageSaveUtil imageSaveUtil;

    @InjectMocks
    private ImageService imageService;

    @Test
    @DisplayName("이미지 파일 저장 테스트")
    public void storeImageFilesTest() {
        //Given
        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("imageFiles", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1".getBytes()),
                new MockMultipartFile("imageFiles", "image2.png", MediaType.IMAGE_PNG_VALUE, "image2".getBytes()),
                new MockMultipartFile("imageFiles", "image2.gif", MediaType.IMAGE_GIF_VALUE, "image2".getBytes())
        );
        ReflectionTestUtils.setField(imageSaveUtil, "uploadRoot", "src/imageSave");
        doNothing().when(imageSaveUtil).saveImageFile(anyString(), any(MockMultipartFile.class));
        //When
        List<String> result = imageService.storeImageFiles(Optional.of(imageFiles));
        //Then
        assertThat(result.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("이미지 파일이 없을 경우 빈 ArrayList를 반환한다.")
    public void storeImageFilesEmptyTest() {
        //Given
        Optional<List<MultipartFile>> empty = Optional.empty();
        //When
        List<String> result = imageService.storeImageFiles(empty);
        //Then
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("이미지 파일 조회 테스트")
    public void getImageFileTest(){
        //Given
        String imageId = "image1.jpg";
        byte[] imageByteArray = "image1".getBytes();
        Image image = new Image(imageId);
        when(imageRepository.findBySavedName(imageId)).thenReturn(Optional.of(image));
        when(imageSaveUtil.getImageByteArray(image)).thenReturn(imageByteArray);
        //When
        byte[] result = imageService.getImageByteArray(imageId);
        //Then
        assertThat(result).isEqualTo(imageByteArray);
    }

    @Test
    @DisplayName("이미지 파일이 없을 경우에 에러를 발생시킨다.")
    public void failGetImageFileTest(){
        //Given
        String imageId = "image1.jpg";
        when(imageRepository.findBySavedName(imageId)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(()-> imageService.getImageByteArray(imageId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당 이미지가 존재하지 않습니다.");
    }
}

