package com.toongather.toongather.domain.keyword.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.keyword.domain.Keyword;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class KeywordJpaRepository {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public KeywordJpaRepository(EntityManager em) {
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public Long save(Keyword keyword){
    if(keyword.getKeywordId() == null){
      em.persist(keyword);
    }else{
      em.merge(keyword);
    }
    return keyword.getKeywordId();
  }

  public Keyword findByKeywordId(String keywordId) {
    return em.find(Keyword.class, keywordId);
  }

}
