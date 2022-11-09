package com.toongather.toongather.domain.review.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name = "WEBTOON_REVIEW")
public class Review extends BaseEntity {

    public Review() {
    }
    @Id
    private String reviewId;

    @ManyToOne
    @JoinColumn(name="toonId")
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name="MEMBER_NO")
    private Member member;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String record;

    @Column(nullable = false)
    private Long star;

    @Column(nullable = false)
    private String amdUserId;

    @Column(nullable = false)
    private LocalDateTime regDt;

    @Column(nullable = false)
    private LocalDateTime amdDt;


}
