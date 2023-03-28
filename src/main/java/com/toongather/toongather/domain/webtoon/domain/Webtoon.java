package com.toongather.toongather.domain.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "WEBTOON")
public class Webtoon extends BaseEntity {

  public Webtoon() {

  }

  @Builder
  public Webtoon(String title, String summary, String writerNm, String age, WebtoonStatus status,
      String imgPath) {
    this.title = title;
    this.summary = summary;
    this.writerNm = writerNm;
    this.age = age;
    this.status = status;
    this.imgPath = imgPath;
  }

  @Id
  @GenericGenerator(name = "seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = SeqGenerator.SEQ_NAME, value = "WEBTOON_SEQ"),
          @org.hibernate.annotations.Parameter(name = SeqGenerator.PREFIX, value = "WT")})
  @GeneratedValue(generator = "seqGenerator")
  private String toonId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 65532)
  private String summary;

  @Column(nullable = false)
  private String writerNm;

  @Column(nullable = false)
  private String age;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WebtoonStatus status;   // END, STOP, ING

  @Column
  private String imgPath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "platformId")
  private Platform platform;

  // 연관관계 메소드

  // 생성 메소드


}
