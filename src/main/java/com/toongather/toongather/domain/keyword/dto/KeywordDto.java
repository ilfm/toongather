package com.toongather.toongather.domain.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class KeywordDto {

  private Long keywordId;
  private String keywordNm;
}
