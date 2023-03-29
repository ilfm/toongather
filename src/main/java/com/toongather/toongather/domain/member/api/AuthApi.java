package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.global.security.jwt.JwtToken;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  /**
   * note access token이 만료됨에따라, refresh token을 확인하여 검증
   * - refresh token 유효한 경우 ->
   *  1) refresh token db 조회하여 일치 하는지 확인
   *  2) 일치한다면 access token 재발급, 일치하지 않는다면 에러
   * - refresh token 만료된 경우
   *  1) 에러발생, 재로그인 요청
   * @param user
   * @param request
   * @return
   */
  @PostMapping("/refresh")
  public ResponseEntity refreshToken(@RequestBody Map<String, String> user, HttpServletRequest request){

    JwtToken tokens = jwtTokenProvider.resolveToken(request);

    //refresh token 검증
    switch (jwtTokenProvider.validateToken(tokens.getRefreshToken())) {
      case DENIED:
      case EXPIRED :
        return new ResponseEntity("login need", HttpStatus.UNAUTHORIZED);
      case ACCESS :
        Long memberId = Long.valueOf(user.get("id"));
        Member member = Optional.ofNullable(memberRepository.findOne(Long.valueOf(memberId)))
          .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        //access token 발급
        if(member.getRefreshToken().equals(tokens.getRefreshToken())) {
          String token = jwtTokenProvider.createToken(member.getId(), member.getMemberRoles());

          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.add("Authorization", "Bearer " + token);

          return new ResponseEntity<>("success", httpHeaders, HttpStatus.OK);
        }
        break;
    }

    return new ResponseEntity("login need", HttpStatus.UNAUTHORIZED);
  }

}
