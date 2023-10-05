package com.fitnesscommerce.domain.post.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_Category_id")
    private PostCategory postCategory;

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

}
