package com.toongather.toongather.domain.review.domain;

import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.review.dto.ReviewSearchDto;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "REVIEW")
public class Review extends BaseEntity {

  public Review() {
  }

  @Id
  @GenericGenerator(name = "seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = SeqGenerator.SEQ_NAME, value = "REVIEW_SEQ"),
          @org.hibernate.annotations.Parameter(name = SeqGenerator.PREFIX, value = "RV")})
  @GeneratedValue(generator = "seqGenerator")
  private String reviewId;

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


  /* 연관관계 편의 메소드 */
  public void setMember(Member member){
    this.member = member;
    //member.getReviews().add(this);
  }
  public void setWebtoon(Webtoon webtoon){
    this.webtoon = webtoon;
  }

  /* 생성 메소드 */
  public static Review createReview(Member member, Webtoon webtoon, ReviewSearchDto reviewData){
    Review review = new Review();

    review.setRecommendComment(reviewData.getRecommendComment());
    review.setStar(reviewData.getStar());
    review.setMember(member);
    review.setWebtoon(webtoon);

    return review;
  }

  @Builder
  public Review(Webtoon toon, Member member, String recommendComment, Long star) {
    this.webtoon = toon;
    this.member = member;
    this.recommendComment = recommendComment;
    this.star = star;
  }

}
