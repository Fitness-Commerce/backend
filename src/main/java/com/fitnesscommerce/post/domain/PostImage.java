package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor //이를 사용하여 객체를 생성할 때 각 필드의 값을 넘겨주면, 생성자 내부에서 필드들이 초기화됩니다
@Table(name = "postImage")
public class PostImage extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fileName", nullable = false)
    private String fileName;  // 파일 원본명

    @Column(name = "url", nullable = false)
    private String url;  // 파일 저장 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(String fileName, String url, Post post) {
        this.fileName = fileName;
        this.url = url;
        this.post = post;
    }


}
