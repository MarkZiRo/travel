package com.swyp6.familytravel.image.service;
import com.swyp6.familytravel.common.util.ImageSaveUtil;
import com.swyp6.familytravel.image.entity.Image;
import com.swyp6.familytravel.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageSaveUtil imageSaveUtil;

    private String storeImageFile(MultipartFile imageFile) {
        String originalFileName = Objects.requireNonNull(imageFile.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + fileExtension;

        Image image = Image.builder()
                .savedName(saveFileName)
                .build();
        imageRepository.save(image);
        imageSaveUtil.saveImageFile(saveFileName, imageFile);
        return saveFileName;
    }

    public List<String> storeImageFiles(Optional<List<MultipartFile>> imageFiles) {
        return imageFiles.orElse(new ArrayList<>())
                .stream()
                .map(this::storeImageFile)
                .toList();
    }

    private void deleteImage(String savedName) {
        Image image = imageRepository.findBySavedName(savedName).orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));
        imageRepository.delete(image);
    }

    public void deleteImageList(List<String> savedNames) {
        savedNames.forEach(this::deleteImage);
    }

    @Transactional(readOnly = true)
    public byte[] getImageByteArray(String imageId) {
        Image image = imageRepository.findBySavedName(imageId).orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));
        return imageSaveUtil.getImageByteArray(image);
    }

}
