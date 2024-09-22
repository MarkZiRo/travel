package com.swyp6.familytravel.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp6.familytravel.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageController.class)
class ImageControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;
    
    @DisplayName("이미지 파일 저장 테스트")
    @Test
    void storeImageFilesTest() throws Exception {
        //Given
        String image = "image1.jpg";
        when(imageService.getImageByteArray(image)).thenReturn(image.getBytes());
        //When
        //Then
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/image/" + image))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andReturn()
                .getResponse();
        byte[] contentAsByteArray = response.getContentAsByteArray();
        assertThat(contentAsByteArray).isEqualTo(image.getBytes());


    }
}