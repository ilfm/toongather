package com.toongather.toongather.domain.review.domain;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;


import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SequenceGenerator(
    name = "REVIEW_SEQ_GEN",
    sequenceName = "REVIEW_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Entity
@Table(name = "REVIEW")
public class Review extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "REVIEW_SEQ_GEN")
  @Id
  private Long reviewId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "toonId", foreignKey = @ForeignKey(name = "fk_review_to_toon"))
  private Webtoon webtoon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_NO", foreignKey = @ForeignKey(name = "fk_review_to_member"))
  private Member member;

  @Column
  private String recommendComment;

  @Column
  private Long star;

  @OneToMany(mappedBy = "review")
  private List<ReviewRecord> records = new ArrayList<>();

  @OneToMany(mappedBy = "review")
  private List<ReviewKeyword> keywords = new ArrayList<>();

  public void setMember(Member member) {
    this.member = member;
  }

  public void setWebtoon(Webtoon webtoon) {
    this.webtoon = webtoon;
  }

  public static Review createReview(Member member, Webtoon webtoon, String recommendComment,
      Long star) {
    return Review.builder()
        .recommendComment(recommendComment)
        .star(star)
        .member(member)
        .toon(webtoon)
        .build();
  }

  public void updateReview(String recommendComment, Long star) {
    this.recommendComment = recommendComment;
    this.star = star;
  }

  @Builder
  public Review(Long reviewId, Webtoon toon, Member member, String recommendComment, Long star) {
    this.reviewId = reviewId;
    this.webtoon = toon;
    this.member = member;
    this.recommendComment = recommendComment;
    this.star = star;
  }
}
