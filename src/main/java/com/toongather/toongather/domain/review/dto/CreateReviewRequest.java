package com.toongather.toongather.domain.review.dto;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateReviewRequest {

  private Long memberId;
  private Long toonId;
  private String recommendComment;
  private Long star;

  public Review toEntity(Member member, Webtoon webtoon) {
    return Review.builder()
        .toon(webtoon)
        .member(member)
        .recommendComment(this.recommendComment)
        .star(this.star)
        .build();
  }
}
