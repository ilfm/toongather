package com.toongather.toongather.domain.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.review.domain.QReview;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.dto.QReviewDto;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.webtoon.domain.QWebtoon;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
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
                w.imgPath,
                w.platform,
                r.recommendComment,
                w.status,
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

  // 리뷰 상세 찾기
  public Review findReviewDetail(String reviewId) {

    QReview r = new QReview("r");
    Review review = jpaQueryFactory.select(r)
        .from(r)
        .where(r.reviewId.eq(reviewId))
        .fetchOne();
    return review;
  }

  // 웹툰 리뷰 찾기
  public Tuple findReview(String reviewId) {

    QReview r = new QReview("r");
    QWebtoon w = new QWebtoon("w");
    Tuple review = (Tuple) jpaQueryFactory.select(r, r.webtoon)
        .from(r)
        .where(r.reviewId.eq(reviewId))
        .fetchOne();

    log.info(review.toString());

    return review;
  }
}
