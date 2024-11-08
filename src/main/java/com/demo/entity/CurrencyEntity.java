package com.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency")
@EntityListeners(AuditingEntityListener.class)
public class CurrencyEntity {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "label")
    private String label;

    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}
