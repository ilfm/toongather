package com.toongather.toongather.domain.member.repository;

import com.toongather.toongather.domain.member.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}
