package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.dto.MemberDTO;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.common.ResponseDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordEncoder passwordEncoder;
  private final AuthService authService;

  @PostMapping("/join")
  public ResponseEntity<ResponseDTO> join(@Valid @RequestBody JoinFormDTO dto) {

    Long id = memberService.join(dto);

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .data(Map.of("id", id))
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/confirm")
  public ResponseEntity<ResponseDTO> confirm(@RequestBody MemberDTO memberDto) {

    memberService.confirmMember(memberDto.getId(), memberDto.getTempCode());

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}/resend-email")
  public ResponseEntity<ResponseDTO> reSendEmail(@PathVariable Long id) {

    memberService.reSendEmail(id);

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @PostMapping("/login")
  public ResponseEntity<ResponseDTO> login(@RequestBody MemberDTO memberDto) {

    MemberDTO member = memberService.loginMember(memberDto.getEmail(), memberDto.getPassword());

    if (member == null) {
      ResponseDTO response = ResponseDTO.builder()
          .message("password failed")
          .build();
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    if (member.getMemberType() == MemberType.TEMP) {
      ResponseDTO response = ResponseDTO.builder()
          .message("temp member")
          .data(Map.of("id", member.getId()))
          .build();
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    //로그인 성공 시, access token 생성
    HttpHeaders httpHeaders = authService.setAccessTokenHeader(member);
    //refresh token 생성 및 저장
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    authService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);
    httpHeaders.add("X-RT_TOKEN", "Bearer " + refreshToken);

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .build();

    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);

  }

  @GetMapping("/search/members")
  public ResponseEntity<ResponseDTO> searchMember(@RequestBody MemberDTO member) {

    ConcurrentMap<String, Object> result = memberService.findMemberByNameAndPhone(
        member.getName(),
        member.getPhone());

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .data(result)
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  // 이메일로 비밀번호 초기화하기
  @GetMapping("/{email}/reset-password")
  public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String email) {

    memberService.resetPasswordByEmail(email);

    ResponseDTO response = ResponseDTO.builder()
        .message("success")
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/test")
  @PreAuthorize("hasAnyRole('ROLE_USER')")
  public String test() {
    return "success";
  }

}
