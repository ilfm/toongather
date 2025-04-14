package com.toongather.toongather.global.config;

import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class DummyDataLodaer {

  private final RoleRepository roleRepository;

  @PostConstruct
  public void initData() {

    Role user = Role.builder()
        .name(RoleType.ROLE_USER)
        .build();

    Role admin = Role.builder()
        .name(RoleType.ROLE_ADMIN)
        .build();

    roleRepository.saveAll(List.of(user, admin));


  }



}
