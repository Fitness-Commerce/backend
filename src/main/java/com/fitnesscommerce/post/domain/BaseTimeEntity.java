package com.fitnesscommerce.post.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseTimeEntity {

    @CreationTimestamp //해당 어노테이션은 엔티티가 처음 저장될 때의 시간을 자동으로 설정합니다.
    private LocalDateTime created_at;

    @UpdateTimestamp //해당 엔티티가 수정될 때마다 시간을 자동으로 설정합니다.
    private LocalDateTime updated_at;

}
