package com.swyp6.familytravel.image.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Test
    void testCreateDate(){
        ReflectionTestUtils.setField(imageService, "uploadRoot", "E:\\imageSave");
        imageService.getImageSaveDir();
    }

    @Test
    void create(){
        ReflectionTestUtils.setField(imageService, "uploadRoot", "E:\\imageSave");
        imageService.storeImageFiles(List.of(new MockMultipartFile("이미지 파일", "test.png", MediaType.IMAGE_PNG_VALUE, "thumbnail".getBytes())));
    }

}

