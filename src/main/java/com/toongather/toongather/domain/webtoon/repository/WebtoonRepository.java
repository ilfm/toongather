package com.toongather.toongather.domain.webtoon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.QMember;
import com.toongather.toongather.domain.webtoon.domain.QWebtoon;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WebtoonRepository {

    @PersistenceContext
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    public WebtoonRepository(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 웹툰 등록
    public void save(Webtoon webtoon){
        em.persist(webtoon);
    }

    // 특정 웹툰 찾기
    public Webtoon findById (String toonId){
        return em.find(Webtoon.class, toonId);
    }

    // 전체 웹툰
    public List<Webtoon> findAll(){
        QWebtoon t = new QWebtoon("t");
         return queryFactory
                .select(t)
                .from(t)
                .fetch();
    }




}
