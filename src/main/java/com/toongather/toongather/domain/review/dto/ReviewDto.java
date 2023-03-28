package com.toongather.toongather.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.MemberDto;
import com.toongather.toongather.domain.webtoon.WebtoonDto;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewDto {

  private String reviewId;
  private MemberDto member;
  private String recommendComment;
  private String record;
  private Long star;
  private WebtoonDto webtoon;

  @QueryProjection
  public ReviewDto(String reviewId, String toonId, String title, String recommendComment,
      String record, Long star, Long memberNo) {

    this.reviewId = reviewId;
    this.webtoon = WebtoonDto.builder().toonId(toonId).title(title).build();
    this.recommendComment = recommendComment;
    this.record = record;
    this.star = star;
    this.member = MemberDto.builder().id(memberNo).build();

  }
}
