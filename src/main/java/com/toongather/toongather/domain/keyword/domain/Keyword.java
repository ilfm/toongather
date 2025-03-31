package com.toongather.toongather.domain.keyword.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;


@Getter
@SequenceGenerator(
    name = "KEYWORD_SEQ_GEN",
    sequenceName = "KEYWORD_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Table(name = "KEYWORD")
@Entity
public class Keyword {

  protected Keyword() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "KEYWORD_SEQ_GEN")
  private Long keywordId;

  @Column
  private String keywordNm;

  @Builder
  public Keyword(Long keywordId, String keywordNm) {
    this.keywordId = keywordId;
    this.keywordNm = keywordNm;
  }

  public static Keyword createKeyword(String keywordNm) {
    return Keyword.builder()
        .keywordNm(keywordNm)
        .build();
  }

}
