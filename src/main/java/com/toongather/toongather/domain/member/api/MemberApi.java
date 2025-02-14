package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.dto.MemberDTO;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApi {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberService memberService;
  private final AuthService authService;

  @PostMapping("/join")
  public Long join(@Valid @RequestBody JoinFormDTO dto) {
    return memberService.join(dto);
  }

  @PostMapping("/confirm")
  public void confirm(@RequestBody MemberDTO memberDto) {
    memberService.confirmMember(memberDto.getId(), memberDto.getTempCode());
  }

  @GetMapping("/{id}/resend-email")
  public void reSendEmail(@PathVariable Long id) {
    memberService.reSendEmail(id);
  }


  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody MemberDTO memberDto) {

    MemberDTO member = memberService.loginMember(memberDto.getEmail(), memberDto.getPassword());

    if (member == null) {
      throw new CommonRuntimeException(CommonError.USER_NOT_PASSWORD);
    }

    if (member.getMemberType() == MemberType.TEMP) {
      throw new CommonRuntimeException(CommonError.USER_NOT_ACTIVE);
    }

    //로그인 성공 시, access token 생성
    HttpHeaders httpHeaders = authService.setAccessTokenHeader(member);
    //refresh token 생성 및 저장
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    authService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);
    httpHeaders.add("X-RT_TOKEN", "Bearer " + refreshToken);

    return new ResponseEntity<>("login success", httpHeaders, HttpStatus.OK);

  }

  @GetMapping("/search/members")
  public ConcurrentMap<String, Object> searchMember(@RequestBody MemberDTO member) {
    return memberService.findMemberByNameAndPhone(
        member.getName(),
        member.getPhone());
  }


  // 이메일로 비밀번호 초기화하기
  @GetMapping("/{email}/reset-password")
  public void resetPassword(@PathVariable String email) {
    memberService.resetPasswordByEmail(email);
  }

  @GetMapping("/test")
  @PreAuthorize("hasAnyRole('ROLE_USER')")
  public String test() {
    return "success";
  }

}
