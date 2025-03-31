package com.toongather.toongather.domain.review.domain;


import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Builder
@Getter
@SequenceGenerator(
    name = "REVIEW_KEYWORD_SEQ_GEN",
    sequenceName = "REVIEW_KEYWORD_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Table(name = "REVIEW_KEYWORD")
@Entity
public class ReviewKeyword extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "REVIEW_KEYWORD_SEQ_GEN")
  private Long reviewKeywordId;

  @ManyToOne
  @JoinColumn(name = "reviewId", foreignKey = @ForeignKey(name = "fk_reviewkeword_to_review"))
  private Review review;

  @ManyToOne
  @JoinColumn(name = "keywordId", foreignKey = @ForeignKey(name = "fk_reviewkeword_to_keyword"))
  private Keyword keyword;

  public static ReviewKeyword createReviewKeyword(Review review, Keyword keyword) {
    return ReviewKeyword.builder()
        .review(review)
        .keyword(keyword)
        .build();
  }
}
