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
  public Webtoon(Long toonId, String title, WebtoonStatus status, String imgPath,
      Platform platform, Age age, String author) {
    this.toonId = toonId;
    this.title = title;
    this.status = status;
    this.imgPath = imgPath;
    this.platform = platform;
    this.age = age;
    this.author = author;
  }
  /*
  * 웹툰 api 사용하기로 함
  * */
  @Id
  @Column(nullable = false)
  private Long toonId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Age age;                // ALL, OVER15, OVER19

  @Column(columnDefinition = "TEXT")
  private String summary;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WebtoonStatus status;   // END, STOP, ING

  @Column
  private String imgPath;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Platform platform;      // NAVER, DAUM, LEZHIN, KAKAO

}
