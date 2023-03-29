package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
  private final MemberRepository memberRepository;

  private final MemberService memberService;

  private final PasswordEncoder passwordEncoder;

  @PostMapping("/join")
  public Long join(@Valid @RequestBody JoinFormDTO dto) {
    Long id = memberService.join(dto);
    return id;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody Map<String, String> user) {

    Long memberId = Long.valueOf(user.get("id"));
    String memberPwd = user.get("password");
    Member member = Optional.ofNullable(memberRepository.findOne(Long.valueOf(memberId)))
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    //패스워드 일치 확인
    if (!passwordEncoder.matches(memberPwd, member.getPassword())) {
      return new ResponseEntity("password failed", HttpStatus.UNAUTHORIZED);
    }

    //accessToken 생성
    String token = jwtTokenProvider.createToken(member.getId(), member.getMemberRoles());

    //refresh token 생성 및 저장
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    memberService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + token);
    httpHeaders.add("X-RT_TOKEN", "Bearer " + refreshToken);

    return new ResponseEntity<>("success", httpHeaders, HttpStatus.OK);

  }

  @GetMapping("/user")
  @PreAuthorize("hasAnyRole('ROLE_USER')")
  public String test() {
    return "success";
  }

}
