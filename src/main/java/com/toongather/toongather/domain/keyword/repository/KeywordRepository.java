package com.toongather.toongather.domain.keyword.repository;


import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.domain.QKeyword;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KeywordRepository {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory jpaQueryFactory;

  public KeywordRepository(EntityManager em) {
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public String save(Keyword keyword){
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

  public Keyword findByKeywordNm(String keywordNm) {
    QKeyword k = new QKeyword("k");
    Tuple keyword = jpaQueryFactory.select(k.keywordId,k.keywordNm).from(k).where(k.keywordNm.eq(keywordNm)).fetchOne();
    return Keyword.builder().keywordId(keyword.get(k.keywordId)).keywordNm(keyword.get(k.keywordNm)).build();
  }
}
