package com.toongather.toongather.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtCode {

    EXPIRED("만료된 JWT"),
    ACCESS("인증된 JWT"),
    DENIED("조작되거나 지원되지 않는 토큰"),

    REFRESH_TOKEN("refresh token"),
    ACCESS_TOKEN("access token");


  private final String message;

}
