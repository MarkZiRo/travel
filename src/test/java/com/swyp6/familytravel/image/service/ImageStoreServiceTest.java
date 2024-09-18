package com.swyp6.familytravel.image.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageStoreServiceTest {

    @InjectMocks
    ImageStoreService imageStoreService;

    @Test
    void testCreateDate(){
        ReflectionTestUtils.setField(imageStoreService, "uploadRoot", "E:\\imageSave");
        imageStoreService.getImageSaveDir();
    }

    @Test
    void create(){
        ReflectionTestUtils.setField(imageStoreService, "uploadRoot", "E:\\imageSave");
        imageStoreService.storeImageFiles(List.of(new MockMultipartFile("이미지 파일", "test.png", MediaType.IMAGE_PNG_VALUE, "thumbnail".getBytes())));
    }

}

