package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberRole;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;


  /**
   * 회원가입
   */
  @Transactional
  public Long join(JoinFormDTO dto) {

    //권한 조회
    Role role = roleRepository.findRoleByName(RoleType.ROLE_USER);

    //리스트로 롤 권한들을 조회했다고 가정.
    //List<Role> roles = new ArrayList<>();


    //회원 저장
    Member member = Member.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .nickName(dto.getNickName())
        .phone(dto.getPhone())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();

    memberRepository.save(member);

    //권한 추가
    List<MemberRole> memberRoles = new ArrayList<>();
    MemberRole memberRole = MemberRole.builder().role(role).member(member).build();
    memberRoles.add(memberRole);
    member.addMemberRoles(role);


    roleRepository.saveMemberRoles(memberRoles);

    return member.getId();

  }

  @Transactional
  public void updateTokenAndLoginHistoryById(Long id, String refreshToken) {
    Member member = memberRepository.findOne(id);
    member.setRefreshToken(refreshToken);
    member.regLastLoginHistory();

  }


}
