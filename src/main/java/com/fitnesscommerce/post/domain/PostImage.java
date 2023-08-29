package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_image")
public class PostImage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;  // 파일 원본명

    private String url;  // 파일 저장 경로

    @Builder
    public PostImage(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }


}
