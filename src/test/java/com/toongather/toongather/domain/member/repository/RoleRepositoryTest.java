package com.toongather.toongather.domain.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RoleRepositoryTest {

  @Autowired
  private RoleRepository roleRepository;

  @Test
  @DisplayName("권한 저장")
  void saveRole() {
    Role role = Role.builder().name(RoleType.ROLE_ADMIN).build();
    roleRepository.saveRole(role);

    Assertions.assertThat(roleRepository.findOne(2l)).isEqualTo(role);

  }

  @Test
  @DisplayName("권한 이름을 통해 도메인 가져오기")
  void findByName() {

    Role role = roleRepository.findRoleByName(RoleType.ROLE_USER);
    Assertions.assertThat(role.getId()).isEqualTo(1l);

  }


}