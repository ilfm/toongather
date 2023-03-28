package com.toongather.toongather.domain.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.review.domain.QReview;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.dto.QReviewDto;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.webtoon.domain.QWebtoon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class ReviewRepository {

  @PersistenceContext
  private final EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public ReviewRepository(EntityManager em) {
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  // 모든 리뷰 찾기
  public List<ReviewDto> findAll() {

    QReview r = new QReview("r");
    QWebtoon w = new QWebtoon("w");
    List<ReviewDto> allReview = jpaQueryFactory.select(
            new QReviewDto(
                r.reviewId,
                r.webtoon.toonId,
                w.title,
                r.recommendComment,
                r.record,
                r.star,
                r.member.id
            )
        )
        .from(r)
        .leftJoin(r.webtoon, w)
        .fetch();

    return allReview;
  }

  public Review findById(String reviewId) {
    return em.find(Review.class, reviewId);
  }

  // 리뷰 저장
  public void save(Review review) {
    if (review.getReviewId() == null) {
      em.persist(review);
    } else {
      // todo merge 문 쓰면 안됨 변경감지로 변경
      em.merge(review);
    }
  }

  // 웹툰 리뷰 찾기
  public Review findReview(Long memberNo, String toonId) {

    QReview r = new QReview("r");
    Review review = jpaQueryFactory.select(r)
        .from(r)
        .where(r.member.id.eq(memberNo)
            .and(r.webtoon.toonId.eq(toonId)))
        .fetchOne();
    return review;
  }


}
