package com.toongather.toongather.global.common;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @LastModifiedDate
    @Column(nullable = false)
    private String regDt;

    @CreatedDate
    @Column(updatable = false)
    private String amdDt;

    @PrePersist // 엔티티 저장하기 전
    void onPrePersist(){
        this.regDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm:ss"));
        this.amdDt = regDt;
    }

    @PreUpdate  // 엔티티 수정하기 전
    void onPreUpdate(){
        this.amdDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm:ss"));
    }
}
