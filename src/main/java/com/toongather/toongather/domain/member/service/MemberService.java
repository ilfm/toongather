package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.MemberRoleRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final MemberRoleRepository memberRoleRepository;
  private final BCryptPasswordEncoder passwordEncoder;


  /**
   * 회원가입
   */
  @Transactional
  public Long join(Member member) {

    //권한 조회
    Role role = roleRepository.findByName(RoleType.ROLE_USER);
    member.addMemberRoles(role);
    memberRepository.save(member);
    memberRoleRepository.saveAll(member.getMemberRoles());

    return member.getId();
  }

  /**
   * 회원조회
   * @param id
   * @return
   */
  public Member findMember(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }


}
