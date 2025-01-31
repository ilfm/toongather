package com.toongather.toongather.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.MemberDto;
import com.toongather.toongather.domain.webtoon.WebtoonDto;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
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
  private Long star;
  private WebtoonDto webtoon;

  @QueryProjection
  public ReviewDto(String reviewId, String toonId, String title,String author,String summary, String imgPath, Platform platform,
      String recommendComment, WebtoonStatus status, Long star, Long memberNo) {

    this.reviewId = reviewId;
    this.webtoon = WebtoonDto.builder().toonId(toonId).title(title).author(author).summary(summary).platform(platform)
        .imgPath(imgPath).status(status).build();
    this.recommendComment = recommendComment;
    this.star = star;
    this.member = MemberDto.builder().id(memberNo).build();

  }
}
