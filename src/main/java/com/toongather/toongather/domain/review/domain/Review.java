package com.toongather.toongather.domain.review.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name = "WEBTOON_REVIEW")
public class Review {

    public Review() {
    }
    @Id
    private String reviewId;

    @Column(nullable = false)
    private String toonId;

    @Column(nullable = false)
    private Long memberId;

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
