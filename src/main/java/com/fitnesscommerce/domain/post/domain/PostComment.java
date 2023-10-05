package com.fitnesscommerce.domain.post.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_Comment")
public class PostComment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder // Setter 어노테이션 외부에서 변경할 수 있고 무결성 보안성에 대비, 순서 무관
    public PostComment(Post post, Member member, String content){
        this.post = post;
        this.member = member;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }

    public void updateComment(String content){
        this.content = content;
        this.updated_at = LocalDateTime.now();
    }


}

