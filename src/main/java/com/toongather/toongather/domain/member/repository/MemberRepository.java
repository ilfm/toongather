package com.toongather.toongather.domain.member.repository;

import com.toongather.toongather.domain.member.domain.Member;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepository {

  private final EntityManager em;

  /**
   * 회원가입 시, 회원 저장
   *
   * @param member
   * @return member id 반환
   */
  @Transactional
  public void save(Member member) {em.persist(member);}

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  /**
   * 가입한 id 찾기
   * @param email
   * @return
   */
  public Member getJoinedMember(String email) {

    try {
      return em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
          .setParameter("email", email).getSingleResult();

    } catch (NoResultException e) {
      return null;
    }

  }

  /**
   * 권한과 함께 id 조회
   * @param memberId
   * @return
   */
  public Member getMemberWithRole(Long memberId) {
    return em
        .createQuery("SELECT m from Member m "
            + "join fetch m.memberRoles mr "
            + "join fetch mr.role where m.id = :id", Member.class)
        .setParameter("id", memberId)
        .getSingleResult();
  }

  public Member getMemberById(Long memberId) {
    return em
        .createQuery("SELECT m from Member m " +
            "where m.id = :id", Member.class)
        .setParameter("id", memberId)
        .getSingleResult();
  }


}
