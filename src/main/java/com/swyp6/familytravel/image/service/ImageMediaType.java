package com.swyp6.familytravel.image.service;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public enum ImageMediaType {
    PNG(".png", MediaType.IMAGE_PNG),
    JPEG(".jpg", MediaType.IMAGE_JPEG),
    JPG(".jpeg", MediaType.IMAGE_JPEG),
    GIF(".gif", MediaType.IMAGE_GIF);

    private final String extension;
    private final MediaType mediaType;

    ImageMediaType(String extension, MediaType mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public static MediaType getMediaTypeByExtension(String imageName) {
        for (ImageMediaType type : values()) {
            if (imageName.endsWith(type.getExtension())) {
                return type.getMediaType();
            }
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
