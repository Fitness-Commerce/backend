package com.fitnesscommerce.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequiredArgsConstructor
public class PostImageController {

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @GetMapping("/api/post/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        Path filePath = Paths.get(fileStorageLocation).resolve(filename).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 파일로 설정

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                throw new RuntimeException("Image was not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Image retrieval failed.");
        }
    }
}
