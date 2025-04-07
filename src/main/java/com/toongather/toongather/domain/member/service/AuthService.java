package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * refresh token 재발급시 db저장 및 기록
   * @param id
   * @param refreshToken
   */
  @Transactional
  public void updateTokenAndLoginHistoryById(Long id, String refreshToken) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    member.setRefreshToken(refreshToken);
    member.regLastLoginHistory();

  }

  /**
   * access token 생성 및 header add
   * @param member
   * @return
   */
  public HttpHeaders setAccessTokenHeader(MemberRequest member) {

    //accessToken 생성
    String token = jwtTokenProvider.createToken(member.getId(), member.getRoleNames());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    return httpHeaders;
  }




}
