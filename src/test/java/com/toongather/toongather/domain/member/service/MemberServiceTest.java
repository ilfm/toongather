package com.toongather.toongather.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.dto.MemberDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.MemberRoleRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import com.toongather.toongather.global.common.error.custom.MemberException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private MemberRoleRepository memberRoleRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private EmailService emailService;

  @InjectMocks
  private MemberService memberService;

  private Member memberEntity;

  private MemberDTO memberDTO;

  @BeforeEach
  void setUp() {

    memberEntity = Member.builder()
        .memberId(1L)
        .name("test")
        .email("test@gmail.com")
        .phone("010-1234-5678")
        .password("1234")
        .nickName("test")
        .build();

    memberEntity.addMemberRoles(Role.builder().name(RoleType.ROLE_USER).build());

  }

  @Test
  @DisplayName("회원가입 성공")
  void join() {
    //given
    JoinFormRequest request = new JoinFormRequest();
    request.setName(memberEntity.getName());
    request.setEmail(memberEntity.getEmail());
    request.setPhone(memberEntity.getPhone());
    request.setPassword(memberEntity.getPassword());
    request.setNickName(memberEntity.getNickName());

    Role role = Role.builder()
        .name(RoleType.ROLE_USER)
        .build();

    given(passwordEncoder.encode(request.getPassword())).willReturn("1234");
    given(roleRepository.findByName(RoleType.ROLE_USER)).willReturn(role);
    given(memberRoleRepository.saveAll(any())).willReturn(List.of());

    //when
    when(memberRepository.save(any(Member.class))).thenAnswer(
        invocation -> {
          Member memberToSave = invocation.getArgument(0);
          return Member.builder()
              .memberId(1L)
              .name(memberToSave.getName())
              .email(memberToSave.getEmail())
              .phone(memberToSave.getPhone())
              .password(memberToSave.getPassword())
              .nickName(memberToSave.getNickName())
              .build();
        }
    );
    Member member = memberService.join(request);

    //then
    assertThat(member.getId()).isNotNull();
    verify(roleRepository).findByName(any());
    verify(memberRoleRepository).saveAll(any());
    verify(memberRepository).save(argThat(memberField -> {
      //필드검증
      return memberField.getName().equals("test") &&
             memberField.getEmail().equals("test@gmail.com") &&
             memberField.getPhone().equals("010-1234-5678") &&
             memberField.getPassword().equals("1234") &&
             memberField.getNickName().equals("test");
    }));

  }

  @Test
  @DisplayName("로그인 성공한다")
  void loginSuccess() {
    String email = memberEntity.getEmail();
    String password = memberEntity.getPassword();

    given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(memberEntity));
    given(passwordEncoder.matches(password, memberEntity.getPassword())).willReturn(true);

    MemberDTO result = memberService.loginMember(email, password);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(memberEntity.getName());

  }

  @Test
  @DisplayName("이름과 폰번호로 회원을 찾을 수 있다.")
  void findMemberByNameAndPhone() {
    // given
    String name = "홍길동";
    String phone = "010-1234-5678";

    ConcurrentMap<String, Object> memberInfo = new ConcurrentHashMap<>();
    memberInfo.put("email", memberEntity.getEmail());
    memberInfo.put("id", 1L);
    given(memberRepository.findByNameAndPhone(name, phone)).willReturn(
        Optional.ofNullable(memberEntity));

    ConcurrentMap<String, Object> result = memberService.findMemberByNameAndPhone(
        name, phone);

    // then
    assertThat(result)
        .isNotNull()
        .containsEntry("email", memberEntity.getEmail())
        .containsEntry("id", 1L);

  }

  @Test
  @DisplayName("이름과 폰번호로 회원을 찾을 수 없어 에러를 던진다.")
  void findMemberByNameAndPhoneFail() {
    // given
    String name = "홍길동";
    String phone = "010-1234-5678";

    given(memberRepository.findByNameAndPhone(name, phone)).willReturn(
        Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> memberService.findMemberByNameAndPhone(name, phone))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("사용자를 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("id를 통해 회원과 권한을 찾는다")
  void findMemberWithRoleById() {
    // given
    Long id = 1L;
    given(memberRepository.findMemberWithRole(id)).willReturn(
        Optional.ofNullable(memberEntity));

    // when
    MemberDTO result = memberService.findMemberWithRoleById(id);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getRoleNames()).contains(RoleType.ROLE_USER.name());

  }

  @Test
  @DisplayName("이메일을 통해서 패스워드가 리셋된다")
  void resetPasswordByEmail() {
    String email = memberEntity.getEmail();

    given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(memberEntity));
    given(passwordEncoder.encode(any())).willReturn(any());

    String password = memberService.resetPasswordByEmail(email);

    assertThat(password).isNotNull();
    verify(passwordEncoder).encode(any());

  }

  @Test
  @DisplayName("회원 id와 임시코드가 일치하면 정회원으로 변경된다")
  void confirmMemberByTempCode() {
    Long id = memberEntity.getId();
    memberEntity.updateTempCode("temp", LocalDateTime.now());
    String tempCode = "temp";

    given(memberRepository.findById(id)).willReturn(Optional.ofNullable(memberEntity));

    memberService.confirmMemberByTempCode(id, tempCode);

    assertThat(memberEntity.getMemberType()).isEqualTo(MemberType.ACTIVE);

  }

  @Test
  @DisplayName("회원 임시코드가 일치하지 않아 에러를 던진다")
  void confirmMemberByTempCodeError() {
    Long id = memberEntity.getId();
    memberEntity.updateTempCode("temp", LocalDateTime.now());
    String tempcode = "not-temp";

    given(memberRepository.findById(id)).willReturn(Optional.ofNullable(memberEntity));

    assertThatThrownBy(() -> memberService.confirmMemberByTempCode(id, tempcode))
        .isInstanceOf(MemberException.class);

  }

  @Test
  @DisplayName("메일을 보낸다")
  void resendEmail() {
    Long id = memberEntity.getId();
    given(memberRepository.findById(id)).willReturn(Optional.ofNullable(memberEntity));
    memberService.resendEmail(id);
    verify(emailService).sendEmail(eq("tempcode"), any(), eq(memberEntity.getEmail()));

  }


}
