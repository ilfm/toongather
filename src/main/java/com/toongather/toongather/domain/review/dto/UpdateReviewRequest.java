package com.toongather.toongather.domain.review.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class UpdateReviewRequest {

  private Long reviewId;
  private String recommendComment;
  private Long star;
  //private List<String> keywords;
}
