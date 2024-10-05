package com.swyp6.familytravel.image.controller;

import com.swyp6.familytravel.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.swyp6.familytravel.image.service.ImageMediaType.getMediaTypeByExtension;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
@Tag(name = "Image")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 다운로드 API", description = "이미지를 다운로드합니다.")
    @GetMapping("/{imageName}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable(name = "imageName") String imageName){
        byte[] imageBytes = imageService.getImageByteArray(imageName);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                .contentType(getMediaTypeByExtension(imageName))
                .contentLength(imageBytes.length)
                .body(resource);
    }
}
