package com.toongather.toongather.repository.member;


import com.toongather.toongather.domain.member.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
