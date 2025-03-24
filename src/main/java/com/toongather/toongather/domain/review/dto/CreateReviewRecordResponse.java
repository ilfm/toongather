package com.toongather.toongather.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateReviewRecordResponse {

  private Long reviewId;
  private Long reviewRecordId;
  private Long memberId;
  private String record;

  @Builder
  public CreateReviewRecordResponse(Long reviewId, Long reviewRecordId, Long memberId,
      String record) {
    this.reviewId = reviewId;
    this.reviewRecordId = reviewRecordId;
    this.memberId = memberId;
    this.record = record;
  }
}
