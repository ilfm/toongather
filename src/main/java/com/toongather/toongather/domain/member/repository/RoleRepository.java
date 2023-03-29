package com.toongather.toongather.domain.member.repository;

import com.toongather.toongather.domain.member.domain.MemberRole;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {
  @PersistenceContext
  EntityManager em;

  public void saveRole(Role role) {
    em.persist(role);
  }

  public void saveMemberRole(MemberRole memberRole) { em.persist(memberRole);}

  public void saveMemberRoles(List<MemberRole> memberRoles) {
    for (MemberRole memberRole : memberRoles) {
      em.persist(memberRole);
    }
  }

  public Role findOne(Long id) { return em.find(Role.class, id);}

  public Role findRoleByName(RoleType type) {

    return em.createQuery("SELECT r from Role r where r.name = :name", Role.class)
        .setParameter("name", type).getSingleResult();
//        return em.find(Role.class, id)
  }


}
