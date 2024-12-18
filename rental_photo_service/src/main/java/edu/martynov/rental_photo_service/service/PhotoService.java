package edu.martynov.rental_photo_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final Path storagePath = Paths.get("photos");

    public String savePhoto(MultipartFile file) {
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = storagePath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return "/api/photos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }

    public byte[] getPhoto(String photoId) {
        try {
            Path filePath = storagePath.resolve(photoId);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Photo not found", e);
        }
    }
}

