package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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
  public Long join(@Valid @RequestBody JoinFormDTO dto) {

    log.info("join", dto);

    //회원 저장
    Member member = Member.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .nickName(dto.getNickName())
        .phone(dto.getPhone())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();
    return memberService.join(member);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody Map<String, String> user) {
    Member member = memberService.findMember(Long.valueOf(user.get("id")));
    String memberPwd = user.get("password");
    //패스워드 일치 확인
    if (!passwordEncoder.matches(memberPwd, member.getPassword())) {
      return new ResponseEntity("password failed", HttpStatus.UNAUTHORIZED);
    }

    //access token 생성
    HttpHeaders httpHeaders = authService.setAccessTokenHeader(member);
    //refresh token 생성 및 저장
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    authService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);
    httpHeaders.add("X-RT_TOKEN", "Bearer " + refreshToken);

    return new ResponseEntity<>("success", httpHeaders, HttpStatus.OK);

  }

  @GetMapping("/user")
  @PreAuthorize("hasAnyRole('ROLE_USER')")
  public String test() {
    return "success";
  }

}
