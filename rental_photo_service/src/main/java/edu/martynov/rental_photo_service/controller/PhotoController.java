package edu.martynov.rental_photo_service.controller;

import edu.martynov.rental_photo_service.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getContentType());
        String url = photoService.savePhoto(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String photoId) {
        byte[] photo = photoService.getPhoto(photoId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }
}
