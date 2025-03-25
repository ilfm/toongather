package com.toongather.toongather.domain.review.dto;


import com.toongather.toongather.domain.review.domain.ReviewRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewRecordResponse {

  Long reviewRecordId;
  Long reviewId;
  String record;

  @Builder
  public ReviewRecordResponse(Long reviewRecordId, Long reviewId, String record) {
    this.reviewRecordId = reviewRecordId;
    this.reviewId = reviewId;
    this.record = record;
  }

  public static ReviewRecordResponse from(ReviewRecord reviewRecord) {
    return ReviewRecordResponse.builder()
        .reviewId(reviewRecord.getReview().getReviewId())
        .reviewRecordId(reviewRecord.getReviewRecordId())
        .record(reviewRecord.getRecord()).build();
  }

}
