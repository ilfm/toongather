package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.security.jwt.JwtToken;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberService memberService;
  private final AuthService authService;

  /**
   * note access token이 만료됨에따라, refresh token을 확인하여 검증
   * - refresh token 유효한 경우 ->
   *  1) refresh token db 조회하여 일치 하는지 확인
   *  2) 일치한다면 access token 재발급, 일치하지 않는다면 에러
   * - refresh token 만료된 경우
   *  1) 에러발생, 재로그인 요청
   * @param id
   * @param request
   * @return
   */
  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(@RequestBody Long id, HttpServletRequest request)
      throws IllegalAccessException {

    JwtToken tokens = jwtTokenProvider.resolveToken(request);

    //refresh token 검증
    switch (jwtTokenProvider.validateToken(tokens.getAccessToken())) {
      case DENIED, EXPIRED -> {
        return new ResponseEntity<>("login need", HttpStatus.UNAUTHORIZED);
      }
      case ACCESS -> {
        MemberRequest member = memberService.findMemberWithRoleById(id);
        //access token 발급
        if(member.getRefreshToken().equals(tokens.getRefreshToken())) {
          HttpHeaders httpHeaders = authService.setAccessTokenHeader(member);
          return new ResponseEntity<>("success", httpHeaders, HttpStatus.OK);
        }
      }
      default -> throw new IllegalAccessException("Unexpected value Result");
    }


    return new ResponseEntity<>("login need", HttpStatus.UNAUTHORIZED);
  }

  @GetMapping("/oauth2/redirect")
  public ResponseEntity<String> oauth2Success(@RequestParam("Authorization") String accessToken,
      @RequestParam("X-RT_TOKEN") String refreshToken) {
    log.info("accessToken token: " + accessToken);
    log.info("Refresh token: " + refreshToken);
    return new ResponseEntity<>("success", HttpStatus.OK);
  }

}
