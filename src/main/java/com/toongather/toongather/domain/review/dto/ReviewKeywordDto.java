package com.toongather.toongather.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewKeywordDto {

  private String reviewKeywordId;
  private String keywordId;
  private String keywordNm;

  @QueryProjection
  public ReviewKeywordDto(String reviewKeywordId, String keywordId, String keywordNm) {
    this.reviewKeywordId = reviewKeywordId;
    this.keywordId = keywordId;
    this.keywordNm = keywordNm;
  }
}
