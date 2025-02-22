package com.toongather.toongather.domain.webtoon;

import com.toongather.toongather.domain.webtoon.domain.Platform;
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

  private Long toonId;
  private String title;
  private String summary;
  private String author;
  private String age;
  private WebtoonStatus status;
  private String imgPath;
  private Platform platform;
}
