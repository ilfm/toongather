package com.toongather.toongather.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.toongather.toongather.domain.member.dto.MemberDto;
import com.toongather.toongather.domain.webtoon.WebtoonDto;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewSearchDto {

  private String reviewId;
  private String title;
  private Long star;
  private String imgPath;           // 웹툰 썸네일 TO-DO 추후 변경
  private String recommendComment;
  private String reviewDate;
}
