package com.toongather.toongather.domain.keyword.domain;

import com.toongather.toongather.SeqGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;


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
