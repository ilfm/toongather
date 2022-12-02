package com.toongather.toongather.domain.member.repository;


import com.toongather.toongather.domain.member.domain.MebmerRole;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * 회원가입 시, 회원 저장
     * @param member
     * @return member id 반환
     */
    public Long save(Member member){
        em.persist(member);
        Role role = findAdminRole();
        MebmerRole memberRole = MebmerRole.builder().role(role).member(member).build();
        em.persist(memberRole);

        return member.getId();
    }

    public Long saveRefreshToken(Member member) {
//        Member member = find(id);
//        member.setRefreshToken(refreshToken);
        em.persist(member);
        return 1l;
    }


    public Role findAdminRole() {return em.find(Role.class, 1);}

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
