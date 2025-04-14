package com.toongather.toongather.global.security.oauth2;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.global.security.jwt.JwtToken;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

//로그인 성공 시 jwt 토큰을 발급하는 부분
@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;
  private final MemberRepository memberRepository;
  private final HttpCookieOAuth2AuthorizationRequestRepository cookieRepository;
  private final String TARGET_URL = "http://localhost:8080/auth/oauth2/redirect";

  @Override
  @Transactional
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    if(authentication instanceof OAuth2AuthenticationToken oauthToken) {

      String targetUrl = determineTargetUrl(request, response, oauthToken);
      JwtToken token = saveUserToken(oauthToken);
      String redirectUrl = getRedirectUrl(TARGET_URL, token);

      if (response.isCommitted()) {
        logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        return;
      }

      getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

  }

  @Override
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirectUri = CookieUtils.getCookie(request,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);
    clearAuthenticationAttributes(request, response);
    return redirectUri.orElse(getDefaultTargetUrl());
  }

  private String getRedirectUrl(String targetUrl, JwtToken token) {
    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam(HttpHeaders.AUTHORIZATION, token.getAccessToken())
        .queryParam(JwtToken.getRefreshTokenName(), token.getRefreshToken())
        .build().toUriString();
  }

  private JwtToken saveUserToken(OAuth2AuthenticationToken oauthToken) {
    OAuth2User user = oauthToken.getPrincipal();
    Member member = memberRepository.findByEmail(user.getAttribute("email"))
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    MemberRequest memberRequest = MemberRequest.builder()
        .id(member.getId())
        .email(member.getEmail())
        .name(member.getName())
        .nickname(member.getNickName())
        .roleNames(member.getMemberRoles()
            .stream()
            .map(t -> t.getRole()
                .getName()
                .name())
            .toList())
        .memberType(member.getMemberType())
        .build();

    HttpHeaders httpHeaders = authService.setAccessTokenHeader(memberRequest);
    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
    authService.updateTokenAndLoginHistoryById(member.getId(), refreshToken);

    return JwtToken.builder()
        .accessToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION))
        .refreshToken(refreshToken)
        .build();
  }


  protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    cookieRepository.removeAuthorizationRequestCookies(request, response);
  }
}
