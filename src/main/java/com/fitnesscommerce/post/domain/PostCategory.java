package com.fitnesscommerce.post.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "postCategory")
public class PostCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;

    @Builder
    public PostCategory(Long id, Long postId, String title){
        this.id = id;
        this.postId = postId;
        this.title = title;
    }
}






