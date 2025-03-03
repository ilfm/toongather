package com.toongather.toongather.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.toongather.toongather.domain.review.domain.ReviewKeyword;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ReviewKeywordJpaRepository {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public ReviewKeywordJpaRepository(EntityManager em){
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public Long save(ReviewKeyword reviewKeyword){
    if(reviewKeyword.getReviewKeywordId() == null){
      em.persist(reviewKeyword);
    }else{
      em.merge(reviewKeyword);
    }
    return reviewKeyword.getReviewKeywordId();
  }


}
