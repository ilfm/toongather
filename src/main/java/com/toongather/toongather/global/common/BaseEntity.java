package com.toongather.toongather.global.common;


import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(updatable = false)
    private String regDt;

    @Column
    private String amdDt;

    @CreatedBy
    @Column(updatable = false)
    private String regUserId;

    @LastModifiedBy
    @Column
    private String amdUserId;

    @PrePersist // 엔티티 저장하기 전
    void onPrePersist(){

        LocalDateTime now =LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.regDt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.amdDt = regDt;
    }

    @PreUpdate  // 엔티티 수정하기 전
    void onPreUpdate(){
        LocalDateTime now =LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.amdDt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

    }
}
