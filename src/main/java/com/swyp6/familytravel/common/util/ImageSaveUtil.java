package com.swyp6.familytravel.common.util;

import com.swyp6.familytravel.image.entity.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class ImageSaveUtil {

    @Value("${images.upload-root}")
    private String uploadRoot;

    private Path getImageSaveDir(String fileName) {
        Path directoryPath = Paths.get(uploadRoot, LocalDate.now().toString());
        if(Files.notExists(directoryPath)){
            try{
                Files.createDirectory(directoryPath);
            }catch (IOException e){
                throw new RuntimeException("이미지 파일 저장 폴더 생성 실패");
            }
        }
        return Paths.get(directoryPath.toString(), fileName);
    }

    public void saveImageFile(String fileName, MultipartFile imageFile) {
        Path imagePath = getImageSaveDir(fileName);
        try {
            Files.copy(imageFile.getInputStream(), imagePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 저장 실패");
        }
    }

    public byte[] getImageByteArray(Image image) {
        Path filePath = Paths.get(uploadRoot, image.getCreatedDateTime().toLocalDate().toString(), image.getSavedName());
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 조회 실패");
        }
    }

    public void deleteImageFile(Image image) {
        Path filePath = Paths.get(uploadRoot, image.getCreatedDateTime().toLocalDate().toString(), image.getSavedName());
        try{
            Files.delete(filePath);
        } catch (IOException e){
            throw new RuntimeException("이미지 파일 삭제 실패");
        }
    }
}
