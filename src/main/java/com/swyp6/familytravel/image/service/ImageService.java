package com.swyp6.familytravel.image.service;
import com.swyp6.familytravel.image.entity.Image;
import com.swyp6.familytravel.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${images.upload-root}")
    private String uploadRoot;

    private final ImageRepository imageRepository;

    private String storeImageFile(MultipartFile imageFile, int order) {
        String originalFileName = Objects.requireNonNull(imageFile.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + fileExtension;

        Image image = Image.builder()
                .savedName(saveFileName)
                .order(order)
                .build();
        imageRepository.save(image);

        Path imageSaveDir = getImageSaveDir();
        Path imagePath = Paths.get(imageSaveDir.toString(), saveFileName);

        try {
            Files.copy(imageFile.getInputStream(), imagePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 저장 실패");
        }
        return saveFileName;
    }

    public List<String> storeImageFiles(Optional<List<MultipartFile>> imageFiles) {
        List<String> result = new ArrayList<>();
        List<MultipartFile> imageList = imageFiles.orElse(new ArrayList<>());
        for(int i = 0; i < imageList.size(); i++){
            result.add(storeImageFile(imageList.get(i), i));
        }
        return result;
    }

    public Path getImageSaveDir(){
        Path directoryPath = Paths.get(uploadRoot, LocalDate.now().toString());
        if(Files.notExists(directoryPath)){
            try{
                Files.createDirectory(directoryPath);
            }catch (IOException e){
                throw new RuntimeException("이미지 파일 저장 폴더 생성 실패");
            }
        }
        return directoryPath;
    }

    public byte[] getImageByteArray(String imageId) {
        Image image = imageRepository.findBySavedName(imageId).orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));
        Path filePath = Paths.get(uploadRoot, image.getCreatedDateTime().toLocalDate().toString(), image.getSavedName());
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
