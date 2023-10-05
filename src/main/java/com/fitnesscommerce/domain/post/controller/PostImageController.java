package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.exception.PostImageNotFound;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/images")
@Tag(name = "게시글 이미지", description = "게시글 이미지 관련 API")
public class PostImageController {

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Operation(summary = "게시글 이미지 조회", description = "게시글 이미지를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveImage(
            @Parameter(name = "filename", description = "이미지 파일 이름", in = ParameterIn.PATH)
            @PathVariable String filename) {

        Path filePath = Paths.get(fileStorageLocation).resolve(filename).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 파일로 설정

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            throw new PostImageNotFound(); // 이미지를 찾을 수 없을 때 PostImageNotFound 예외를 발생시킴
        }
    }
}
