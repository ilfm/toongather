package com.toongather.toongather.domain.review.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchReviewResponse {

  private Long reviewId;
  private Long memberId;
  private Long toonId;
  private String title;
  private Long star;
  private String imgPath;           // 웹툰 썸네일 TO-DO 추후 변경
  private String recommendComment;
  private String reviewDate;
  private List<String> keywords;

  public SearchReviewResponse(Long reviewId, String title, Long star, String imgPath,
      String recommendComment, String reviewDate, List<String> keywords) {
    this.reviewId = reviewId;
    this.title = title;
    this.star = star;
    this.imgPath = imgPath;
    this.recommendComment = recommendComment;
    this.reviewDate = reviewDate;
    this.keywords = keywords;
  }

}
