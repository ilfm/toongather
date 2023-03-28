package com.toongather.toongather.domain.webtoon;

import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WebtoonDto {

  private String toonId;
  private String title;
  private String summary;
  private String writerNm;
  private String age;
  private WebtoonStatus status;
  private String imgPath;
}
