package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postCategory_id", referencedColumnName = "id")
    private PostCategory postCategory;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;

    @Column(name = "title") // 제목
    private String title;

    @Column(name = "content") // 내용
    private String content;

    @Column(name = "viewCount") // 조회 수
    private Long viewCount;


    @Builder
    public Post(Member member, PostCategory postCategory, List<PostImage> postImages,
                String title, String content, Long viewCount) {
        this.member = member;
        this.postCategory = postCategory;
        this.postImages = postImages;
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
    }

    public void changePost(String title, String content, List<PostImage> postImages) {
        this.title = title;
        this.content = content;

        if (this.postImages != null) {
            // 기존의 이미지들을 제거
            this.postImages.clear();
        }

        if (postImages != null) {
            // 새로운 이미지들 추가
            this.postImages.addAll(postImages);
            for (PostImage postImage : postImages) {
                postImage.setPost(this);
            }
        }
    }
}

