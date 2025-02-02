package com.toongather.toongather.domain.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.review.domain.QReview;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewSortType;

import com.toongather.toongather.domain.review.dto.ReviewSearchDto;
import com.toongather.toongather.domain.webtoon.domain.QWebtoon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class ReviewJpaRepository {

  @PersistenceContext
  private final EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public ReviewJpaRepository(EntityManager em) {
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public List<ReviewSearchDto> searchWithSortType(ReviewSortType sort){
    return null;
  }

  // 모든 리뷰 찾기
  public List<ReviewSearchDto> findAll() {

    QReview r = new QReview("r");
    QWebtoon w = new QWebtoon("w");
//    List<ReviewSearchDto> allReview = jpaQueryFactory.select(
//            new QReviewDto(
//                r.reviewId,
//                r.webtoon.toonId,
//                w.title,
//                w.author,
//                w.summary,
//                w.imgPath,
//                w.platform,
//                r.recommendComment,
//                w.status,
//                r.star,
//                r.member.id
//            )
//        )
//        .from(r)
//        .leftJoin(r.webtoon, w)
//        .fetch();

    //return allReview;
    return null;
  }

  public Review findById(String reviewId) {
    return em.find(Review.class, reviewId);
  }

  public Review findByToonId(String toonId) {
    QReview r = new QReview("r");
    Review review = jpaQueryFactory.select(r)
        .from(r)
        .where(r.webtoon.toonId.eq(toonId))
        .fetchOne();

    return review;
  }

  // 리뷰 저장
  public void save(Review review) {
    if (review.getReviewId() == null) {
      em.persist(review);
      em.flush();

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
  public ReviewSearchDto findReview(String reviewId) {

    QReview r = new QReview("r");
    QWebtoon w = new QWebtoon("w");
//    ReviewSearchDto review = jpaQueryFactory.select(
//            new QReviewDto(
//                r.reviewId,
//                r.webtoon.toonId,
//                w.title,
//                w.author,
//                w.summary,
//                w.imgPath,
//                w.platform,
//                r.recommendComment,
//                w.status,
//                r.star,
//                r.member.id
//            )
//        )
//        .from(r)
//        .leftJoin(r.webtoon, w)
//        .where(r.reviewId.eq(reviewId))
//        .fetchOne();
//    return review;
    return null;
  }

}
