package com.fitnesscommerce.domain.post.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor
public class PostImage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //멤버 변수
    private Long id;

    private String fileName;  // 파일 원본명

    private String url;  // 파일 저장 경로

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY) //Post와 PostImage 사이의 관계가 다대일 관계임을 나타냅니다.
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id") //외래 키 칼럼의 이름을 지정합니다.
    private Post post;

    @Builder
    public PostImage(String fileName, String url, Post post) {
        this.fileName = fileName;
        this.url = url;
        this.post = post;
    }

    public void setPost(Post post){
        this.post = post;
    }


}
