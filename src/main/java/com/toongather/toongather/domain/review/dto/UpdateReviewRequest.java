package com.toongather.toongather.domain.review.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class UpdateReviewRequest {

  private String reviewId;
  private String recommendComment;
}
