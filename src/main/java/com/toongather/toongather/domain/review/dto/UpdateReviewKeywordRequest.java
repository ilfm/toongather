package com.toongather.toongather.domain.review.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateReviewKeywordRequest {

  private Long reviewId;
  private List<String> keywords;

}
