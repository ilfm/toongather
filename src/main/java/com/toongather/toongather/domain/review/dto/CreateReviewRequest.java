package com.toongather.toongather.domain.review.dto;

import lombok.Getter;

@Getter
public class CreateReviewRequest {

  private String reviewId;
  private Long memberNo;
  private String recommendComment;
  private String record;
  private Long star;
  private String toonId;
}
