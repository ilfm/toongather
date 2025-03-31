package com.toongather.toongather.global.config;

import com.toongather.toongather.global.security.jwt.JwtAccessDeniedHandler;
import com.toongather.toongather.global.security.jwt.JwtAuthenticationEntryPoint;
import com.toongather.toongather.global.security.jwt.JwtAuthenticationFilter;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import com.toongather.toongather.global.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.toongather.toongather.global.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.toongather.toongather.global.security.oauth2.UserOAuth2Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 기본적인 웹보안 활성화
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  private final UserOAuth2Service userOAuth2Service;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  private final String[] PERMIT_ALL = {"/member/join", "/member/login", "/oauth2/**"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //cors
    http.cors(httpSecurityCorsConfigurer ->
      httpSecurityCorsConfigurer.configurationSource(request -> {
      var cors = new CorsConfiguration();
      cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
      cors.setAllowedHeaders(List.of("*"));
      return cors;
    }));

    //jwt 토큰 방식으로 로그인
    http.sessionManagement(httpSessionManagement ->
        httpSessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //crsf, 로그인에서 formlogin, formbasic 방식 제외
    http.csrf(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);

    //url 권한 관리
    http.authorizeHttpRequests(authz -> {
      authz.requestMatchers(PERMIT_ALL).permitAll();
      authz.anyRequest().authenticated();
    });

    //oaurh2 소셜로그인 적용
    http.oauth2Login(login -> {
      login.authorizationEndpoint(endpoint ->
          endpoint.baseUri("/oauth2/authorize"));
      login.userInfoEndpoint(endpoint ->
          endpoint.userService(userOAuth2Service));
      login.redirectionEndpoint(endpoint ->
          endpoint.baseUri("/oauth2/callback/*"));
      login.successHandler(oAuth2AuthenticationSuccessHandler);
      login.failureHandler(oAuth2AuthenticationFailureHandler);
    });

    //jwt filter를 통해 토큰 관리 및 사용자 정보 가져오는 필터 적용
    http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    //security exception시 handling
    http.exceptionHandling(exception -> {
      exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
      exception.accessDeniedHandler(jwtAccessDeniedHandler);
    });

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}




