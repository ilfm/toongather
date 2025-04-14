package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.dto.MemberResponse;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.MemberRoleRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import com.toongather.toongather.global.common.error.custom.MemberException;
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
  public Member join(JoinFormRequest dto) {

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
    Member savedMember = memberRepository.save(member);

    //인증 메일 보내기
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tempExpired = now.plusMinutes(2);
    String tempCode = createCode();
    savedMember.updateTempCode(tempCode, tempExpired);

    return savedMember;
  }

  public MemberRequest loginMember(String email, String password) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new CommonRuntimeException(CommonError.USER_NOT_PASSWORD);
    }
    return new MemberRequest(member);
  }


  public ConcurrentMap<String, Object> findMemberByNameAndPhone(String name, String phone) {
    Member member = memberRepository.findByNameAndPhone(name, phone)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
    result.put("email", member.getEmail());
    result.put("id", member.getId());

    return result;
  }

  public MemberRequest findMemberWithRoleById(Long id) {
    Member member = memberRepository.findMemberWithRole(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    return new MemberRequest(member);
  }

  public Member findMemberEntityById(Long id) {
    return memberRepository.findMemberWithRole(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

  }

  @Transactional
  public String resetPasswordByEmail(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    String password = createCode();
    member.resetPassword(passwordEncoder.encode(password));

    return password;

  }

  @Transactional
  public void confirmMemberByTempCode(Long id, String tempCode) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    if (!member.getTempCode().equals(tempCode)) {
      throw new MemberException.TempCodeInvalidException();
    }
    if (!member.getTempCodeExpired().isBefore(LocalDateTime.now())) {
      throw new MemberException.TempCodeExpiredException();
    }

    member.updateActiveMember();
  }

  @Transactional
  public void resendEmail(Long id) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tempExpired = now.plusMinutes(2);
    String tempCode = createCode();
    member.updateTempCode(tempCode, tempExpired);
    emailService.sendEmail("tempcode", tempCode, member.getEmail());
  }


  public MemberResponse findMemberById(Long id) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    return MemberResponse.from(member);
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
