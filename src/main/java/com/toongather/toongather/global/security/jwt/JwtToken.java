package com.toongather.toongather.global.security.jwt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JwtToken {

  private String refreshToken;
  private String accessToken;


  @Builder
  public JwtToken(String refreshToken, String accessToken) {
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
  }
}
