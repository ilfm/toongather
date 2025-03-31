package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest.LoginRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest.SearchMemberRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest.TempCodeRequest;
import com.toongather.toongather.domain.member.dto.MemberResponse;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.EmailService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.common.ApiResponse;
import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  private final EmailService emailService;
  private final AuthService authService;

  @PostMapping("/join")
  public Long join(@Valid @RequestBody JoinFormRequest dto) {
    Member member = memberService.join(dto);

    if(member.getId() != null) {
      emailService.sendEmail("tempcode", member.getTempCode(), member.getEmail());
    }

    return member.getId();
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request) {

    MemberRequest member = memberService.loginMember(request.getEmail(), request.getPassword());

    if (member.getMemberType() == MemberType.TEMP) {
      throw new CommonRuntimeException(CommonError.USER_NOT_ACTIVE);
    }

    //로그인 성공 시, access token 생성
    HttpHeaders httpHeaders = authService.setAccessTokenHeader(member);
    //refresh token 생성 및 저장
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    authService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken);

    ApiResponse<Object> result = ApiResponse.builder()
        .path("/member/login")
        .data(Map.of("id", member.getId()))
        .message("login success")
        .build();
    return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);

  }

  /**
   * 임시 회원을 정식 회원으로 등록
   * 이메일에서 온 tempCode를 확인
   * @param request
   */
  @PostMapping("/confirm")
  public void confirm(@RequestBody TempCodeRequest request) {
    memberService.confirmMemberByTempCode(request.getId(), request.getTempCode());
  }

  @GetMapping("/{id}/resend-email")
  public void reSendEmail(@PathVariable(name = "id") Long id) {
    memberService.resendEmail(id);
  }


  @GetMapping("/search/members")
  public ConcurrentMap<String, Object> searchMember(@RequestBody SearchMemberRequest member) {
    return memberService.findMemberByNameAndPhone(member.getName(), member.getPhone());
  }

  @GetMapping("/{id}")
  public MemberResponse getMember(@PathVariable(name = "id") Long id) {
    return memberService.findMemberById(id);
  }


  // 이메일로 비밀번호 초기화하기
  @GetMapping("/{email}/reset-password")
  public void resetPassword(@PathVariable(name = "email") String email) {
    String password = memberService.resetPasswordByEmail(email);
    emailService.sendEmail("resetpwd", password, email);
  }

}
