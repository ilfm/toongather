package com.toongather.toongather.domain.member.repository;

import com.toongather.toongather.domain.member.domain.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleRepository {
  @PersistenceContext
  EntityManager em;

  public Long saveAdmin(Role role) {
    em.persist(role);
    return 1l;
  }


}
