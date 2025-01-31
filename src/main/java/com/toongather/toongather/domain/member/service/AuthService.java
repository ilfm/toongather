package com.toongather.toongather.domain.member.service;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    Member member = memberRepository.findById(id).get();
    member.setRefreshToken(refreshToken);
    member.regLastLoginHistory();

  }

  /**
   * access token 생성 및 header add
   * @param member
   * @return
   */
  public HttpHeaders setAccessTokenHeader(Member member) {

    //accessToken 생성
    String token = jwtTokenProvider.createToken(member.getId(), member.getMemberRoles());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + token);

    return httpHeaders;
  }


}
