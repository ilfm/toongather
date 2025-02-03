package com.toongather.toongather.domain.member.repository;

import com.toongather.toongather.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
  @EntityGraph(attributePaths = {"memberRoles"})
  @Query("select m from Member m where m.id = :id")
  Optional<Member> findMemberWithRole(@Param("id") Long id);

  Optional<Member> findByNameAndPhone(String name, String phone);
}
