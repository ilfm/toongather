package com.toongather.toongather.domain.review.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewRecordDto {

  private Long reviewId;
  private Long reviewRecordId;
  private String record;
  private String amdDt;

}
