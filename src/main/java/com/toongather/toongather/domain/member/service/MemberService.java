package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.dto.MemberDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.MemberRoleRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
  private final EmailService emailService;
  private final BCryptPasswordEncoder passwordEncoder;


  /**
   * 회원가입
   */
  @Transactional
  public Long join(JoinFormDTO dto) {

    //회원 저장
    Member member = Member.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .nickName(dto.getNickName())
        .phone(dto.getPhone())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();

    //권한 조회
    Role role = roleRepository.findByName(RoleType.ROLE_USER);
    member.addMemberRoles(role);
    memberRepository.save(member);
    memberRoleRepository.saveAll(member.getMemberRoles());

    //인증 메일 보내기
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tempExpired = now.plusMinutes(2);
    String tempCode = createCode();
    member.updateTempCode(tempCode, tempExpired);
    emailService.sendEmail("tempcode", tempCode, member.getEmail());

    return member.getId();
  }

  public MemberDTO loginMember(String email, String password) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    if (!passwordEncoder.matches(password, member.getPassword())) {
      return null;
    }
    return new MemberDTO(member);
  }


  public ConcurrentMap<String, Object> findMemberByNameAndPhone(String name, String phone) {
    Member member = memberRepository.findByNameAndPhone(name, phone)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
    result.put("email", member.getEmail());
    result.put("id", member.getId());

    return result;
  }

  public MemberDTO findMemberById(Long id) {
    Member member = memberRepository.findMemberWithRole(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    return new MemberDTO(member);
  }

  public void resetPasswordByEmail(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    String password = createCode();
    member.setPassword(passwordEncoder.encode(password));

    emailService.sendEmail("resetpwd", password, member.getEmail());

  }

  public void confirmMemberByTempCode(Long id, String tempCode) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    if (!member.getTempCode().equals(tempCode)) {
      throw new IllegalArgumentException("임시번호가 일치하지 않습니다.");
    }
    if (member.getTempCodeExpired().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("임시번호가 만료되었습니다.");
    }
    member.updateTempCode(null, null);
    member.setMemberType(MemberType.ACTIVE);
  }

  public void reSendEmail(Long id) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tempExpired = now.plusMinutes(2);
    String tempCode = createCode();
    member.updateTempCode(tempCode, tempExpired);
    emailService.sendEmail("tempcode", tempCode, member.getEmail());
  }

  // 인증번호 및 임시 비밀번호 생성 메서드
  public String createCode() {
    Random random = new Random();
    StringBuilder key = new StringBuilder();

    for (int i = 0; i < 8; i++) {
      int index = random.nextInt(4);

      switch (index) {
        case 0 -> key.append((char) (random.nextInt(26) + 97));
        case 1 -> key.append((char) (random.nextInt(26) + 65));
        default -> key.append(random.nextInt(9));
      }
    }
    return key.toString();
  }

}
