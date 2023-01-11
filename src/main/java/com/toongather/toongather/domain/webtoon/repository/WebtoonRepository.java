package com.toongather.toongather.domain.webtoon.repository;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class WebtoonRepository {

    @PersistenceContext
    private EntityManager em;

    // 웹툰 등록
    public void save(Webtoon webtoon){
        em.persist(webtoon);
    }

    // 특정 웹툰 찾기
    public Webtoon findById (String toonId){
        return em.find(Webtoon.class, toonId);
    }



}
