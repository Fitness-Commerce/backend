package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_category")
public class PostCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String title;

    private LocalDateTime createdAt;

    @Builder
    public PostCategory(Long id, Post post, String title, LocalDateTime createdAt){
        this.id = id;
        this.post = post;
        this.title = title;
        this.createdAt = createdAt;
    }
}






