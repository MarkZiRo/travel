package com.swyp6.familytravel.image.controller;

import com.swyp6.familytravel.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.swyp6.familytravel.image.service.ImageMediaType.getMediaTypeByExtension;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

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
