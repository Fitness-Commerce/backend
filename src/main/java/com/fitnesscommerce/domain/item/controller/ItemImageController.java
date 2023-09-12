package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.exception.ImageNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 이미지", description = "상품 이미지 관련 API")
public class ItemImageController {

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Operation(summary = "상품 이미지 반환", description = "상품 이미지 반환 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    @GetMapping("/api/item/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(fileStorageLocation).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 파일로 설정
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                throw new ImageNotFound();
            }
        } catch (Exception e) {
            throw new ImageNotFound();
        }
    }
}
