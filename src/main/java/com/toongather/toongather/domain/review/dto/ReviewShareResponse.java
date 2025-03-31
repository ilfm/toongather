package com.toongather.toongather.domain.review.dto;

import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewShareResponse {

  Long reviewId;
  Long star;
  String title;
  String recommendComment;
  Platform platform;

  @Builder
  public ReviewShareResponse(Long reviewId, Long star, String title,
      String recommendComment, Platform platform) {
    this.reviewId = reviewId;
    this.star = star;
    this.title = title;
    this.recommendComment = recommendComment;
    this.platform = platform;
  }

  public static ReviewShareResponse from(Review review) {
    return ReviewShareResponse.builder()
        .title(review.getWebtoon().getTitle())
        .platform(review.getWebtoon().getPlatform())
        .recommendComment(review.getRecommendComment())
        .reviewId(review.getReviewId())
        .star(review.getStar())
        .build();
  }
}
