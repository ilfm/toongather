package com.toongather.toongather.global.security.jwt;

import com.toongather.toongather.global.common.error.CommonError;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 토큰의 인증정보를 security context에 저장하는 역할 filter
   *
   * @param request
   * @param response
   * @param chain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    //헤더에서 토큰 가져오기
    JwtToken token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
    String servletPath = ((HttpServletRequest) request).getServletPath();

    //login 후 모든 api의 요청에 대해서 jwt 토큰을 검사 하는 필터.
    // 0. 로그인 후 access token과 refresh token이 발급되며 이후 access token만 헤더에 보내 api를 요청
    // 1. api 요청시 access token이 유효한지 확인
    // -> 1) 만료 됐을 경우 refresh token 보낼 것을(expired 응답으로 프론트가 확인)요청, 2) 승인됐을 경우 권한정보 넣어주기
    // 2. access token이 만료되어 refresh token을 보냈을 경우, 이 경우에는 refresh token만 보낸다고 가정한다.
    // ->  /api/v1/refesh 요청인 경우 token 검증은 하지 않도록 함.

    if (servletPath.contains("/auth/refresh") || servletPath.contains("/member/login")
        || servletPath.contains("/member/join")) {
      chain.doFilter(request, response);
    } else if (token.getAccessToken() != null && token.getRefreshToken() == null) {
      //access token에 따른 검증
      switch (jwtTokenProvider.validateToken(token.getAccessToken())) {
        case EXPIRED -> request.setAttribute("exception", CommonError.JWT_EXPIRED);
        case DENIED -> request.setAttribute("exception", CommonError.JWT_DENIED);
        case ACCESS -> {
          Authentication authentication = jwtTokenProvider.getAuthentication(
              token.getAccessToken());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
      chain.doFilter(request, response);
    } else {
      log.error("Invalid request = {}", request);
      log.error("Invalid {}", response.getWriter().toString());
      throw new IllegalStateException();
    }
  }
}
