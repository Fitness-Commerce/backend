package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity{

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

    private int viewCount; // 조회 수


    @Builder
    public Post(Member member, PostCategory postCategory, List<PostImage> postImages,
                String title, String content, int viewCount) {
        this.member = member;
        this.postCategory = postCategory;
        this.postImages = (postImages != null) ? postImages : new ArrayList<>();
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
    }


    public void changePost(PostCategory postCategory, String title, String content, List<PostImage> newImages) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();

        if (newImages != null){
            this.postImages.addAll(newImages);
        }
    }
}

