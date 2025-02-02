package com.toongather.toongather.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.keyword.domain.QKeyword;
import com.toongather.toongather.domain.review.domain.QReviewKeyword;

import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.dto.QReviewKeywordDto;
import com.toongather.toongather.domain.review.dto.ReviewKeywordDto;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ReviewKeywordRepository {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public ReviewKeywordRepository (EntityManager em){
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public String save(ReviewKeyword reviewKeyword){
    if(reviewKeyword.getReviewKeywordId() == null){
      em.persist(reviewKeyword);
    }else{
      em.merge(reviewKeyword);
    }
    return reviewKeyword.getReviewKeywordId();
  }

  /*
  * 나의 키워드 조회
  * */
  public List<ReviewKeywordDto> findMyKeywordByReviewId(String reviewId){
    QReviewKeyword rk = new QReviewKeyword("rk");
    QKeyword k = new QKeyword("k");

    return jpaQueryFactory.select(
        new QReviewKeywordDto(
            rk.reviewKeywordId,
            k.keywordId,
            k.keywordNm
        )
    ).from(rk).leftJoin(rk.keyword,k).where(rk.review.reviewId.eq(reviewId)).fetch();
  }

}
