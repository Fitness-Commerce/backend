package com.fitnesscommerce.domain.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCategory_id")
    private PostCategory postCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;

    private String title;  // 제목

    private String content; // 내용

    private Integer viewCount; // 조회 수

    private LocalDateTime created_at;

    private LocalDateTime updated_at;


    @Builder
    public Post(Member member, PostCategory postCategory, String title, String content, Integer viewCount) {
        this.member = member;
        this.postCategory = postCategory;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.created_at = LocalDateTime.now();
    }


    public void updatePost(PostCategory postCategory, String title, String content) {
        this.postCategory = postCategory;
        this.title = title;
        this.content = content;
        this.updated_at = LocalDateTime.now();
    }

    public void addPostImage(PostImage postImage){
        this.postImages.add(postImage);
    }
}

