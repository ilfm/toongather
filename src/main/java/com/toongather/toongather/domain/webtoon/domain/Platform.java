package com.toongather.toongather.domain.webtoon.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "PLATFORM")
public class Platform extends BaseEntity {

    public Platform() {

    }

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
                      parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="PLATFORM_SEQ"),
                                   @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="PF")} )
    @GeneratedValue(generator = "seqGenerator")
    private String platformId;

    @Column(nullable = false)
    private String platformNm;

    @Column(nullable = false)
    private String regUserId;

    @Column(nullable = false)
    private String amdUserId;


}
