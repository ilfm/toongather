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
  public void flush(){
    em.flush();
  }
  /*
   * 정렬 기준에 따라 리뷰 목록을 조회하는 메서드
   *  :주어진 정렬 타입(ReviewSortType)에 따라 정렬된 리뷰 목록을 반환합니다.
   *  -정렬 기준: 별점 오름차순(STAR_ASC), 별점 내림차순(STAR_DESC), 최신순(CREATE_DATE_DESC)
   *  -만약 sort가 null이거나 정의되지 않은 값이면 기본 정렬(최신순) 적용
   *
   * @param sort 정렬 기준 (ReviewSortType)
   * @return 정렬된 ReviewSearchDto 리스트
   */
  public List<ReviewSearchDto> findAllWithSortType(ReviewSortType sort){

    String sql = "select new com.toongather.toongather.domain.review.dto.ReviewSearchDto"
        + "(r.reviewId,r.webtoon.title,r.star,r.webtoon.imgPath,r.recommendComment,r.regDt) from Review r";

    if(sort == null){
      sql += " order by r.regDt desc";
    }else{
      switch (sort){
        case STAR_ASC:
          sql +=" order by r.star asc";
          break;
        case STAR_DESC:
          sql +=" order by r.star desc";
          break;
        case CREATE_DATE_DESC:
          sql += " order by r.regDt desc";
          break;
        default:
          sql += " order by r.regDt desc";
      }
    }
    return em.createQuery(sql,ReviewSearchDto.class)
             .getResultList();
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
