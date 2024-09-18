package com.swyp6.familytravel.image.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageStoreService {

    @Value("${images.upload-root}")
    private String uploadRoot;

    private String storeImageFile(MultipartFile imageFile) {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + fileExtension;

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
        return imageFiles.orElse(new ArrayList<>()).stream().map(this::storeImageFile).toList();
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
}
